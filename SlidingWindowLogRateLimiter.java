package ratelimiter;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

// Sliding Window Log Algorithm Implementation
// Maintains a log of request timestamps and removes old entries
public class SlidingWindowLogRateLimiter implements RateLimiter {
    private final int maxRequests;
    private final long windowSizeInMillis;
    private final Map<String, Queue<Long>> userRequestLogs;

    public SlidingWindowLogRateLimiter(int maxRequests, long windowSizeInSeconds) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInSeconds * 1000;
        this.userRequestLogs = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();
        Queue<Long> requestLog = userRequestLogs.computeIfAbsent(userId, 
            k -> new ConcurrentLinkedQueue<>());
        
        // Remove old requests outside the window
        while (!requestLog.isEmpty() && 
               currentTime - requestLog.peek() >= windowSizeInMillis) {
            requestLog.poll();
        }
        
        if (requestLog.size() < maxRequests) {
            requestLog.offer(currentTime);
            return true;
        }
        
        return false;
    }
}
