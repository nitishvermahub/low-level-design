package ratelimiter;

// Demonstration of different rate limiting algorithms
public class RateLimiterDemo {
    public static void main(String[] args) {
        run();
    }
    
    public static void run() {
        System.out.println("=== Rate Limiter Demo ===\n");
        
        // Demo 1: Token Bucket Rate Limiter
        System.out.println("1. Token Bucket Rate Limiter (5 tokens, 2 tokens/sec):");
        demoTokenBucket();
        
        // Demo 2: Sliding Window Log Rate Limiter
        System.out.println("\n2. Sliding Window Log Rate Limiter (3 requests per 5 seconds):");
        demoSlidingWindowLog();
        
        // Demo 3: Fixed Window Counter Rate Limiter
        System.out.println("\n3. Fixed Window Counter Rate Limiter (4 requests per 10 seconds):");
        demoFixedWindowCounter();
        
        // Demo 4: Leaky Bucket Rate Limiter
        System.out.println("\n4. Leaky Bucket Rate Limiter (capacity 3, leak rate 1/sec):");
        demoLeakyBucket();
    }
    
    private static void demoTokenBucket() {
        RateLimiter rateLimiter = new TokenBucketRateLimiter(5, 2);
        String userId = "user1";
        
        // Make 7 requests rapidly
        for (int i = 1; i <= 7; i++) {
            boolean allowed = rateLimiter.allowRequest(userId);
            System.out.println("Request " + i + ": " + (allowed ? "ALLOWED" : "DENIED"));
        }
        
        // Wait and try again
        try {
            Thread.sleep(2000); // Wait 2 seconds for tokens to refill
            System.out.println("After 2 seconds...");
            boolean allowed = rateLimiter.allowRequest(userId);
            System.out.println("Request 8: " + (allowed ? "ALLOWED" : "DENIED"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private static void demoSlidingWindowLog() {
        RateLimiter rateLimiter = new SlidingWindowLogRateLimiter(3, 5);
        String userId = "user2";
        
        // Make 5 requests
        for (int i = 1; i <= 5; i++) {
            boolean allowed = rateLimiter.allowRequest(userId);
            System.out.println("Request " + i + ": " + (allowed ? "ALLOWED" : "DENIED"));
        }
    }
    
    private static void demoFixedWindowCounter() {
        RateLimiter rateLimiter = new FixedWindowCounterRateLimiter(4, 10);
        String userId = "user3";
        
        // Make 6 requests
        for (int i = 1; i <= 6; i++) {
            boolean allowed = rateLimiter.allowRequest(userId);
            System.out.println("Request " + i + ": " + (allowed ? "ALLOWED" : "DENIED"));
        }
    }
    
    private static void demoLeakyBucket() {
        RateLimiter rateLimiter = new LeakyBucketRateLimiter(3, 1);
        String userId = "user4";
        
        // Make 5 requests rapidly
        for (int i = 1; i <= 5; i++) {
            boolean allowed = rateLimiter.allowRequest(userId);
            System.out.println("Request " + i + ": " + (allowed ? "ALLOWED" : "DENIED"));
        }
        
        // Wait and try again
        try {
            Thread.sleep(2000); // Wait 2 seconds for bucket to leak
            System.out.println("After 2 seconds...");
            boolean allowed = rateLimiter.allowRequest(userId);
            System.out.println("Request 6: " + (allowed ? "ALLOWED" : "DENIED"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
