package ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Fixed Window Counter Algorithm Implementation
// Divides time into fixed windows and counts requests per window
public class FixedWindowCounterRateLimiter implements RateLimiter {
    private final int maxRequests;
    private final long windowSizeInMillis;
    private final Map<String, WindowCounter> userCounters;

    public FixedWindowCounterRateLimiter(int maxRequests, long windowSizeInSeconds) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInSeconds * 1000;
        this.userCounters = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();
        long currentWindow = currentTime / windowSizeInMillis;
        
        WindowCounter counter = userCounters.computeIfAbsent(userId, 
            k -> new WindowCounter(currentWindow, 0));
        
        // Reset counter if we're in a new window
        if (counter.getWindowId() < currentWindow) {
            counter.setWindowId(currentWindow);
            counter.setCount(0);
        }
        
        if (counter.getCount() < maxRequests) {
            counter.setCount(counter.getCount() + 1);
            return true;
        }
        
        return false;
    }

    // Inner class to track window and count
    private static class WindowCounter {
        private long windowId;
        private int count;

        public WindowCounter(long windowId, int count) {
            this.windowId = windowId;
            this.count = count;
        }

        public long getWindowId() {
            return windowId;
        }

        public void setWindowId(long windowId) {
            this.windowId = windowId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
