package ratelimiter;

/**
 * Enum representing different types of rate limiting algorithms that can be created
 * by the RateLimiterFactory.
 */
public enum RateLimiterType {
    /**
     * Fixed Window Counter algorithm
     * - Simple implementation with fixed time windows
     * - May allow bursts at window boundaries
     */
    FIXED_WINDOW,
    
    /**
     * Sliding Window Log algorithm
     * - More accurate rate limiting using timestamps
     * - Better handles edge cases at window boundaries
     */
    SLIDING_WINDOW_LOG,
    
    /**
     * Token Bucket algorithm
     * - Allows for burst handling
     * - Smooths out traffic spikes
     */
    TOKEN_BUCKET,
    
    /**
     * Leaky Bucket algorithm
     * - Processes requests at a fixed rate
     * - Good for smoothing out traffic
     */
    LEAKY_BUCKET
}
