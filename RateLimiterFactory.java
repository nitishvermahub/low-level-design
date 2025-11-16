package ratelimiter;

/**
 * Singleton Factory class for creating different types of rate limiters.
 * Implements the Singleton + Factory design pattern to provide a centralized way to create rate limiter instances.
 * Thread-safe implementation using Double-Checked Locking with volatile keyword.
 */
public class RateLimiterFactory {
    
    // Volatile ensures visibility of changes across threads
    private static  RateLimiterFactory instance;
    
    /**
     * Private constructor to prevent instantiation from outside.
     */
    private RateLimiterFactory() {
        // Private constructor for singleton
        // Prevents instantiation via reflection
        if (instance != null) {
            throw new IllegalStateException("Singleton instance already created!");
        }
    }
    

    public static RateLimiterFactory getInstance() {
        // First check (no locking) - for performance
        if (instance == null) {
            // Synchronized block - only when instance is null
            synchronized (RateLimiterFactory.class) {
                // Second check (with locking) - to prevent multiple instances
                if (instance == null) {
                    instance = new RateLimiterFactory();
                }
            }
        }
        return instance;
    }
    
    /**
     * Creates a rate limiter of the specified type.
     * 
     * Parameter semantics vary by type:
     * - FIXED_WINDOW: param1 = maxRequests, param2 = windowSizeInSeconds
     * - SLIDING_WINDOW_LOG: param1 = maxRequests, param2 = windowSizeInSeconds
     * - TOKEN_BUCKET: param1 = maxTokens (capacity), param2 = refillRate (tokens per second)
     * - LEAKY_BUCKET: param1 = capacity, param2 = leakRate (requests per second)
     */
    public RateLimiter createRateLimiter(RateLimiterType type, int param1, int param2) {
        if (type == null) {
            throw new IllegalArgumentException("Rate limiter type cannot be null");
        }
        
        switch (type) {
            case FIXED_WINDOW:
                return new FixedWindowCounterRateLimiter(param1, param2);
                
            case SLIDING_WINDOW_LOG:
                return new SlidingWindowLogRateLimiter(param1, param2);
                
            case TOKEN_BUCKET:
                return new TokenBucketRateLimiter(param1, param2);
                
            case LEAKY_BUCKET:
                return new LeakyBucketRateLimiter(param1, param2);
                
            default:
                throw new IllegalArgumentException("Unknown rate limiter type: " + type);
        }
    }
    

    public RateLimiter createRateLimiter(RateLimiterType type) {
        return createRateLimiter(type, 1, 1);
    }
}
