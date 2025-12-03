package com.api.metrics;

import java.util.LinkedList;

public class ServerMetrics {

    // request latencies last 50 calls
    public static LinkedList<Long> latencies = new LinkedList<>();

    // How many errors happened in last 50 calls
    public static LinkedList<Boolean> errors = new LinkedList<>();

    public static synchronized void recordLatency(long ms) {
        if (latencies.size() >= 250) latencies.removeFirst();
        latencies.add(ms);
    }

    public static synchronized void recordError(boolean isError) {
        if (errors.size() >= 250) errors.removeFirst();
        errors.add(isError);
    }

    public static double getAverageLatency() {
        if (latencies.isEmpty()) return 0;
        return latencies.stream().mapToLong(v -> v).average().orElse(0);
    }

    public static int getRecentErrorCount() {
        int count = 0;
        for (boolean e : errors) if (e) count++;
        return count;
    }

    public static int getTotalRequestsTracked() {
        return latencies.size();
    }
}
