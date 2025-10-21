# API Gateway

The **API Gateway** module implements an API Gateway based on **Spring Cloud Gateway**, serving as the **central entry
point** for all microservices in the architecture.
It handles **routing**, **resilience**, **rate limiting**, **caching**, **aggregation**, and **monitoring**, improving
both the **security** and **performance** of the distributed system.

## Key Features

* Routes incoming requests to backend microservices.
* Manages **fallbacks** and **circuit breakers** for fault tolerance.
* Implements **distributed rate limiting** using Redis.
* Supports **response caching** configurable per route.
* Provides **aggregation** of multiple HTTP responses into a single response.
* Offers detailed **logging** and **metrics** via Spring Actuator.

## Adding a New Route

Each route in the gateway defines a set of rules that determine **when** a request should be routed and **how** it
should be processed.
Routes are configured under the `spring.cloud.gateway.routes` section of the `application.yml` file.

A route consists of:

* **id** – Unique identifier for the route (used for logging and management).
* **uri** – Destination backend service (or `no://op` for internal/aggregated routes).
* **predicates** – Matching conditions (e.g., `Path`, `Method`, `Host`).
* **filters** – Transformations or controls applied to requests and responses.

## Filters

**Filters** in Spring Cloud Gateway allow manipulation of requests and responses as they pass through the gateway.
They can be configured for each route in the `spring.cloud.gateway.routes[].filters` section.

Filters can be either **standard** (provided by Spring Cloud Gateway) or **custom** (defined within the project).

## Standard Filters

### CircuitBreaker

The **CircuitBreaker** filter protects backend services from repeated failures and instability.
When a service consistently returns errors over a certain period, the circuit “opens,” preventing further requests from
reaching the backend.
The gateway can then return an **alternative response** or redirect requests to a **fallback** endpoint.

```yaml
filters:
  - name: CircuitBreaker
    args:
      name: exampleCircuitBreaker
      fallbackUri: forward:/fallback
```

**Main Parameters:**

* `name` – Logical name of the circuit (identifies the resilience configuration).
* `fallbackUri` – Local path to redirect the request in case of fallback (typically handled by a `/fallback`
  controller).

**Example Behavior:**

* If `http://t8-controller` times out or returns errors several times,
  → subsequent requests are immediately redirected to `/fallback` until the circuit closes again.

### RequestRateLimiter

The **RequestRateLimiter** filter enforces a **limit on the number of requests** per client, based on a dynamically
resolved key (e.g., the client’s IP address).
In this setup, Redis is used as a distributed store and implements a **token bucket** algorithm.

**Main Parameters:**

* `replenishRate` – Number of tokens added to the bucket per second.
* `burstCapacity` – Maximum number of tokens the bucket can hold (e.g., `5` allows an initial burst of 5 requests).
* `requestedTokens` – Number of tokens consumed per request.
* `key-resolver` – Defines how to compute the unique key for each user or IP (in this case, based on the client’s IP
  address).

**Example Behavior:**

```yaml
filters:
  - name: RequestRateLimiter
    args:
      redis-rate-limiter.replenishRate: 1
      redis-rate-limiter.burstCapacity: 1
      redis-rate-limiter.requestedTokens: 1
      key-resolver: "#{@ipKeyResolver}"
```

* Each IP receives 1 token per second, and each request costs 1 → one request per second.
* If a client sends more than one request within the same second, the gateway responds with **HTTP 429 Too Many Requests
  **.

### RewritePath

The **RewritePath** filter dynamically rewrites the request URI before forwarding it to the backend.
It helps maintain a consistent public API structure while routing to services with different internal paths.

```yaml
filters:
  - RewritePath=/gamerepo/(?<segment>.*), /${segment}
```

**Explanation:**

* The pattern `/gamerepo/(?<segment>.*)` captures everything following `/gamerepo/`.
* `${segment}` is then reinserted as the new path.
  Example:

    * Incoming request: `/gamerepo/list`
    * Backend receives: `/list`

---

## Custom Filters

### RedisCacheFilter

The **RedisCacheFilter** is a custom filter that implements **HTTP response caching in Redis**.
It reduces latency and backend load by temporarily storing responses for a configurable duration.

```yaml
filters:
  - name: RedisCacheFilter
    args:
      ttl: 60
      cachePrefix: "test_cache:"
      methods: "GET, POST, PUT"
```

**Parameters:**

* `ttl` – Cache time-to-live (in seconds). After this period, the cached response is invalidated.
* `cachePrefix` – Redis key prefix used to separate cached data for different routes.
* `methods` – HTTP methods to cache (usually `GET`, but others can be included).

**How It Works:**

1. The gateway builds a Redis key, e.g. `test_cache:/api/data`.
2. If a cached response is found, it’s returned immediately.
3. If not, the gateway forwards the request to the backend and stores the response for the next 60 seconds.

### Aggregation

The **Aggregation** filter combines multiple HTTP responses into a single aggregated response.
It’s useful for endpoints that need to compose data from multiple microservices (the *API Composition* pattern).

```yaml
filters:
  - name: Aggregation
    args:
      services: "http://httpbin.org/get, http://httpbin.org/bytes/10"
```

**Parameters:**

* `services` – A list of URLs to call in parallel. Their responses are merged into a single JSON payload.

**How It Works:**

* The gateway performs multiple HTTP calls in parallel to the specified services.
* It collects their responses and merges them into a JSON array or an aggregated object.
* A single HTTP response is then returned to the client.

**Example of Aggregated Response:**

```json
{
  "results": [
    { "url": "http://httpbin.org/get", "data": { ... } },
    { "url": "http://httpbin.org/bytes/10", "data": "..." }
  ]
}
```

## Complete Route Example

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: example-route
          uri: http://example-service:8080
          predicates:
            - Path=/example/**
            - Method=GET,POST
          filters:
            # Riscrive il path rimuovendo il prefisso /example/
            - RewritePath=/example/(?<segment>.*), /${segment}

            # Applica rate limiting per IP
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 5
                redis-rate-limiter.burstCapacity: 10
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@ipKeyResolver}"

            # Abilita resilienza con fallback locale
            - name: CircuitBreaker
              args:
                name: exampleCircuitBreaker
                fallbackUri: forward:/fallback

            # Caching delle risposte per 30 secondi
            - name: RedisCacheFilter
              args:
                ttl: 30
                cachePrefix: "example_cache:"
                methods: "GET"
```

## Simplify Architecture

```text
             ┌──────────────────────┐
             │      Client API      │
             └─────────┬────────────┘
                       │
             ┌─────────▼────────────┐
             │  Spring Cloud Gateway│
             │  ─────────────────── │
             │  • Routing            │
             │  • Filters (std/custom)│
             │  • Rate Limiting      │
             │  • Cache / Aggregation│
             │  • Circuit Breaker    │
             └─────────┬────────────┘
                       │
     ┌─────────────────┼─────────────────┐
     ▼                 ▼                 ▼
 [UserService]   [GameRepoService]   [CompileService]
```

---

## Monitoring and Actuator

Spring Boot management endpoints are enabled to provide visibility into the gateway:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

**Examples:**

* `/actuator/gateway/routes` → lists active routes and their filters.
* `/actuator/metrics` → metrics tagged with `application=apiGateway`.
* `/actuator/health` → gateway health status.

## Recommendations

* Each route should have at least one `Path` predicate.
* Filters execute in order: always place security or rate-limiting filters **before** transformation filters.
* The cache prefix (`cachePrefix`) should be unique per route to avoid collisions in Redis.
* For routes calling unreliable services, always use the **CircuitBreaker** filter with a `fallbackUri`.
