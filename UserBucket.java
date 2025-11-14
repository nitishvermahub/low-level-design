package ratelimiter;

// Represents a token bucket for a specific user
public class UserBucket {
    private int tokens;
    private long lastRefillTimestamp;
    private final int maxTokens;
    private final int refillRate;

    public UserBucket(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public long getLastRefillTimestamp() {
        return lastRefillTimestamp;
    }

    public void setLastRefillTimestamp(long lastRefillTimestamp) {
        this.lastRefillTimestamp = lastRefillTimestamp;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public int getRefillRate() {
        return refillRate;
    }
}
