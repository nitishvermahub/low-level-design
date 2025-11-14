package ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Token Bucket Algorithm Implementation
// Tokens are added at a fixed rate and consumed per request
public class TokenBucketRateLimiter implements RateLimiter {
    private final int maxTokens;
    private final int refillRate; // tokens per second
    private final Map<String, UserBucket> userBuckets;

    public TokenBucketRateLimiter(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.userBuckets = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized boolean allowRequest(String userId) {
        UserBucket bucket = userBuckets.computeIfAbsent(userId, 
            k -> new UserBucket(maxTokens, refillRate));
        
        refillBucket(bucket);
        
        if (bucket.getTokens() > 0) {
            bucket.setTokens(bucket.getTokens() - 1);
            return true;
        }
        
        return false;
    }

    private void refillBucket(UserBucket bucket) {
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - bucket.getLastRefillTimestamp();
        int tokensToAdd = (int) ((timePassed / 1000.0) * bucket.getRefillRate());
        
        if (tokensToAdd > 0) {
            bucket.setTokens(Math.min(bucket.getMaxTokens(), 
                bucket.getTokens() + tokensToAdd));
            bucket.setLastRefillTimestamp(currentTime);
        }
    }
}
