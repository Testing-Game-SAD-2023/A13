package limiter

import (
	"log"
	"net"
	"net/http"
	"sync"
	"time"

	"golang.org/x/time/rate"
)

type client struct {
	limiter  *rate.Limiter
	lastSeen time.Time
}

type ClientLimiter struct {
	mu      sync.Mutex
	clients map[string]*client
	burst   int
	maxRate float64
}

func NewClientLimiter(burst int, maxRate float64) *ClientLimiter {
	return &ClientLimiter{
		mu:      sync.Mutex{},
		clients: make(map[string]*client),
		burst:   burst,
		maxRate: maxRate,
	}
}

func (cm *ClientLimiter) Cleanup(timeout time.Duration) {
	cm.mu.Lock()
	defer cm.mu.Unlock()

	for ip, client := range cm.clients {
		if time.Since(client.lastSeen) > timeout {
			delete(cm.clients, ip)
		}
	}
}

func (cm *ClientLimiter) getOrAdd(ip string) *rate.Limiter {

	cm.mu.Lock()
	defer cm.mu.Unlock()
	var limiter *rate.Limiter
	v := cm.clients[ip]
	if v != nil {
		v.lastSeen = time.Now()
		limiter = v.limiter
	} else {
		limiter = rate.NewLimiter(rate.Limit(cm.maxRate), cm.burst)
		cm.clients[ip] = &client{
			limiter:  limiter,
			lastSeen: time.Now(),
		}
	}

	return limiter
}

func (cm *ClientLimiter) Limit(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Get the IP address for the current user.
		ip, _, err := net.SplitHostPort(r.RemoteAddr)
		if err != nil {
			log.Print(err.Error())
			http.Error(w, "Internal Server Error", http.StatusInternalServerError)
			return
		}

		// Call the getVisitor function to retreive the rate limiter for
		// the current user.
		limiter := cm.getOrAdd(ip)
		if !limiter.Allow() {
			http.Error(w, http.StatusText(http.StatusTooManyRequests), http.StatusTooManyRequests)
			return
		}

		next.ServeHTTP(w, r)
	})
}
