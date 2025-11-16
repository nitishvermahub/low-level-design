package ratelimiter;

// Interface for rate limiting strategies
public interface RateLimiter {
    boolean allowRequest(String userId);
}
