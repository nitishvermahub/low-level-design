# Rate Limiter

## Overview
A comprehensive implementation of various rate limiting algorithms in Java. Rate limiting is a technique used to control the rate of requests sent or received by a system, preventing abuse and ensuring fair resource allocation.

## Components

### 1. RateLimiter Interface
The base interface that all rate limiting algorithms implement.
- `allowRequest(String userId)`: Determines if a request from a user should be allowed

### 2. Rate Limiting Algorithms

#### Token Bucket Algorithm (`TokenBucketRateLimiter`)
**How it works:**
- Each user has a bucket with a maximum capacity of tokens
- Tokens are added to the bucket at a fixed rate (refill rate)
- Each request consumes one token
- If no tokens are available, the request is denied

**Use cases:**
- Handling bursty traffic while maintaining an average rate
- API rate limiting with burst allowance

**Configuration:**
- `maxTokens`: Maximum number of tokens in the bucket
- `refillRate`: Number of tokens added per second

#### Sliding Window Log Algorithm (`SlidingWindowLogRateLimiter`)
**How it works:**
- Maintains a log of timestamps for each request
- When a new request arrives, removes all timestamps outside the time window
- Allows the request if the number of timestamps is below the limit

**Use cases:**
- Precise rate limiting without edge cases
- When accuracy is more important than memory efficiency

**Configuration:**
- `maxRequests`: Maximum number of requests allowed
- `windowSizeInSeconds`: Time window in seconds

#### Fixed Window Counter Algorithm (`FixedWindowCounterRateLimiter`)
**How it works:**
- Time is divided into fixed windows
- Counts requests in each window
- Resets counter when entering a new window

**Use cases:**
- Simple rate limiting with low memory overhead
- When approximate rate limiting is acceptable

**Configuration:**
- `maxRequests`: Maximum requests per window
- `windowSizeInSeconds`: Duration of each window

**Note:** May allow bursts at window boundaries (e.g., max requests at end of one window + max at start of next)

#### Leaky Bucket Algorithm (`LeakyBucketRateLimiter`)
**How it works:**
- Requests are added to a bucket with fixed capacity
- Requests "leak" out at a constant rate
- If bucket is full, new requests are denied

**Use cases:**
- Smoothing out bursty traffic
- Enforcing a steady output rate

**Configuration:**
- `capacity`: Maximum number of requests the bucket can hold
- `leakRate`: Number of requests that leak per second

## Class Diagram

```
┌─────────────────┐
│  RateLimiter    │ (Interface)
│  <<interface>>  │
├─────────────────┤
│ + allowRequest()│
└────────┬────────┘
         │
         │ implements
         │
    ┌────┴────────────────────────────────────────┐
    │                                              │
┌───┴──────────────────┐              ┌───────────┴──────────────┐
│ TokenBucketRate      │              │ SlidingWindowLog         │
│ Limiter              │              │ RateLimiter              │
├──────────────────────┤              ├──────────────────────────┤
│ - maxTokens          │              │ - maxRequests            │
│ - refillRate         │              │ - windowSizeInMillis     │
│ - userBuckets        │              │ - userRequestLogs        │
├──────────────────────┤              ├──────────────────────────┤
│ + allowRequest()     │              │ + allowRequest()         │
│ - refillBucket()     │              └──────────────────────────┘
└──────────────────────┘
         │                                         │
         │                                         │
┌────────┴──────────────┐              ┌──────────┴───────────────┐
│ FixedWindowCounter    │              │ LeakyBucketRate          │
│ RateLimiter           │              │ Limiter                  │
├───────────────────────┤              ├──────────────────────────┤
│ - maxRequests         │              │ - capacity               │
│ - windowSizeInMillis  │              │ - leakRate               │
│ - userCounters        │              │ - userBuckets            │
├───────────────────────┤              ├──────────────────────────┤
│ + allowRequest()      │              │ + allowRequest()         │
└───────────────────────┘              │ - leak()                 │
                                       └──────────────────────────┘
```

## Usage Example

```java
// Token Bucket: Allow 10 requests initially, refill 2 per second
RateLimiter tokenBucket = new TokenBucketRateLimiter(10, 2);
boolean allowed = tokenBucket.allowRequest("user123");

// Sliding Window Log: Allow 100 requests per 60 seconds
RateLimiter slidingWindow = new SlidingWindowLogRateLimiter(100, 60);
boolean allowed = slidingWindow.allowRequest("user456");

// Fixed Window Counter: Allow 50 requests per 30 seconds
RateLimiter fixedWindow = new FixedWindowCounterRateLimiter(50, 30);
boolean allowed = fixedWindow.allowRequest("user789");

// Leaky Bucket: Capacity of 20, leak 5 per second
RateLimiter leakyBucket = new LeakyBucketRateLimiter(20, 5);
boolean allowed = leakyBucket.allowRequest("user101");
```

## Algorithm Comparison

| Algorithm | Memory Usage | Accuracy | Burst Handling | Complexity |
|-----------|-------------|----------|----------------|------------|
| Token Bucket | Low | Good | Excellent | O(1) |
| Sliding Window Log | High | Excellent | Good | O(n) |
| Fixed Window Counter | Low | Fair | Poor | O(1) |
| Leaky Bucket | Low | Good | Fair | O(1) |

## Thread Safety
All implementations use `ConcurrentHashMap` for thread-safe operations and synchronized methods to ensure consistency in multi-threaded environments.

## Running the Demo
```java
RateLimiterDemo.run();
```

The demo showcases all four algorithms with different configurations and request patterns.

## Design Patterns Used
- **Strategy Pattern**: Different rate limiting algorithms implement the same interface
- **Factory Pattern**: Can be extended to create rate limiters based on configuration
- **Singleton Pattern**: Can be applied to create single instances per configuration

## Future Enhancements
- Distributed rate limiting using Redis
- Sliding window counter (hybrid approach)
- Rate limiting with multiple tiers
- Metrics and monitoring integration
- Configuration-based rate limiter factory
