package ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Leaky Bucket Algorithm Implementation
// Requests leak out at a constant rate, similar to water leaking from a bucket
public class LeakyBucketRateLimiter implements RateLimiter {
    private final int capacity;
    private final int leakRate; // requests per second
    private final Map<String, LeakyBucket> userBuckets;

    public LeakyBucketRateLimiter(int capacity, int leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.userBuckets = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized boolean allowRequest(String userId) {
        LeakyBucket bucket = userBuckets.computeIfAbsent(userId, 
            k -> new LeakyBucket(capacity, leakRate));
        
        leak(bucket);
        
        if (bucket.getCurrentSize() < bucket.getCapacity()) {
            bucket.setCurrentSize(bucket.getCurrentSize() + 1);
            return true;
        }
        
        return false;
    }

    private void leak(LeakyBucket bucket) {
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - bucket.getLastLeakTimestamp();
        int requestsToLeak = (int) ((timePassed / 1000.0) * bucket.getLeakRate());
        
        if (requestsToLeak > 0) {
            bucket.setCurrentSize(Math.max(0, bucket.getCurrentSize() - requestsToLeak));
            bucket.setLastLeakTimestamp(currentTime);
        }
    }

    // Inner class to represent a leaky bucket for a user
    private static class LeakyBucket {
        private int currentSize;
        private long lastLeakTimestamp;
        private final int capacity;
        private final int leakRate;

        public LeakyBucket(int capacity, int leakRate) {
            this.capacity = capacity;
            this.leakRate = leakRate;
            this.currentSize = 0;
            this.lastLeakTimestamp = System.currentTimeMillis();
        }

        public int getCurrentSize() {
            return currentSize;
        }

        public void setCurrentSize(int currentSize) {
            this.currentSize = currentSize;
        }

        public long getLastLeakTimestamp() {
            return lastLeakTimestamp;
        }

        public void setLastLeakTimestamp(long lastLeakTimestamp) {
            this.lastLeakTimestamp = lastLeakTimestamp;
        }

        public int getCapacity() {
            return capacity;
        }

        public int getLeakRate() {
            return leakRate;
        }
    }
}
