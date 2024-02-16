package api

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"strings"

	"github.com/go-chi/chi/v5"
)

const (
	MaxUploadSize   = 2 * (1 << 20) // 2MB
	DefaultBodySize = 1 << 18       // 256KB

)

// ApiError represents the http error returned by the REST service.
// Implements error interface.
type ApiError struct {
	code    int
	Message string `json:"message"`
	err     error
}

func (ae ApiError) Error() string {
	return ae.Message
}

type ApiFunction func(http.ResponseWriter, *http.Request) error

type Validable interface {
	Validate() error
}

func FromJsonBody[T Validable](r io.ReadCloser) (T, error) {

	var t T

	if err := json.NewDecoder(r).Decode(&t); err != nil {
		code := http.StatusBadRequest
		message := "Invalid json body"
		if err, ok := err.(*http.MaxBytesError); ok {
			code = http.StatusRequestEntityTooLarge
			message = fmt.Sprintf("allowed body size: %s", byteCountIEC(err.Limit))
		}
		return t, ApiError{
			code:    code,
			Message: message,
			err:     err,
		}
	}
	defer r.Close()

	if err := t.Validate(); err != nil {
		return t, ApiError{
			code:    http.StatusBadRequest,
			Message: err.Error(),
			err:     err,
		}
	}

	return t, nil
}

type Parseable[T any] interface {
	Parse(s string) (T, error)
}

func FromUrlParams[T Parseable[T]](r *http.Request, name string) (T, error) {
	s := chi.URLParam(r, name)
	return fromString[T](s, name)
}

func FromUrlQuery[T Parseable[T]](r *http.Request, name string, fallback T) (T, error) {
	s := r.URL.Query().Get(name)
	if s == "" {
		return fallback, nil
	}
	return fromString[T](s, name)
}

func fromString[T Parseable[T]](s, name string) (T, error) {
	var t T

	v, err := t.Parse(s)
	if err != nil {
		err = fmt.Errorf("%w %q: %v", ErrInvalidParam, name, err)
		return t, ApiError{
			code:    http.StatusBadRequest,
			err:     err,
			Message: err.Error(),
		}
	}

	return v, nil
}

func WithMaximumBodySize(n int64) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			r.Body = http.MaxBytesReader(w, r.Body, n)
			next.ServeHTTP(w, r)
		})
	}
}

func WriteJson(w http.ResponseWriter, statusCode int, v any) error {
	w.Header().Add("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	return json.NewEncoder(w).Encode(v)
}

func HandlerFunc(f ApiFunction) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if err := f(w, r); err != nil {
			apiError, ok := err.(ApiError)

			if ok {
				if apiError.code == http.StatusInternalServerError {
					log.Print(apiError.err)
				}
				if err := WriteJson(w, apiError.code, apiError); err != nil {
					log.Print(err)
				}
				return
			}

			w.WriteHeader(http.StatusInternalServerError)
			log.Print(err)
		}
	}
}

type JWTAuthenticationConfig struct {
	HeaderKey    string
	Method       string
	AuthEndpoint string
}

func WithJWTAuthentication(c JWTAuthenticationConfig) func(http.Handler) http.Handler {
	type authRequest struct {
		AccessToken string `json:"access_token"`
	}
	return func(h http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			authHeaderParts := strings.Split(r.Header.Get(c.HeaderKey), "Bearer ")
			if len(authHeaderParts) != 2 {
				http.Error(w, http.StatusText(http.StatusUnauthorized), http.StatusUnauthorized)
				return
			}
			token := authHeaderParts[1]

			if token == "" {
				http.Error(w, http.StatusText(http.StatusUnauthorized), http.StatusUnauthorized)
				return
			}

			var body bytes.Buffer
			if err := json.NewEncoder(&body).Encode(authRequest{AccessToken: token}); err != nil {
				http.Error(w, http.StatusText(http.StatusInternalServerError), http.StatusInternalServerError)
				log.Print(err)
				return
			}

			req, err := http.NewRequestWithContext(r.Context(), c.Method, c.AuthEndpoint, &body)
			if err != nil {
				http.Error(w, http.StatusText(http.StatusInternalServerError), http.StatusInternalServerError)
				log.Print(err)
				return
			}
			resp, err := http.DefaultClient.Do(req)
			if err != nil {
				http.Error(w, http.StatusText(http.StatusInternalServerError), http.StatusInternalServerError)
				log.Print(err)
				return
			}
			defer resp.Body.Close()
			if resp.StatusCode != http.StatusOK {
				http.Error(w, http.StatusText(http.StatusUnauthorized), http.StatusUnauthorized)
				return
			}
			h.ServeHTTP(w, r)
		})
	}
}
