package com.api.controller;

import com.api.config.FailureConfig;
import com.api.metrics.ServerMetrics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.util.*;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> getHealth() {

        Map<String, Object> resp = new HashMap<>();
        Map<String, Object> metrics = new HashMap<>();

        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

        long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();

        // JVM memory
        long heapUsed = memoryBean.getHeapMemoryUsage().getUsed() / (1024 * 1024);
        long heapMax = memoryBean.getHeapMemoryUsage().getMax() / (1024 * 1024);
        double memoryUsedPercent = (heapUsed * 100.0) / heapMax;

        // CPU
        double cpuLoad = 0;
        try {
            cpuLoad = (double) osBean.getClass().getMethod("getSystemCpuLoad").invoke(osBean) * 100;
        } catch (Exception ignored) {}

        // Base metrics from REAL request activity
        double avgLatency = ServerMetrics.getAverageLatency();
        int recentErrors = ServerMetrics.getRecentErrorCount();
        int totalRequests = ServerMetrics.getTotalRequestsTracked();

        // If latency slider used, override latency
        if (FailureConfig.artificialDelayMs > 0) {
            avgLatency = FailureConfig.artificialDelayMs;
        }

        // If critical failures injected, increase errors
        if (FailureConfig.dbFailure || FailureConfig.nullPointer || FailureConfig.timeoutFailure) {
            recentErrors = recentErrors + 5;  // force degrade
            resp.put("status", "DEGRADED");
        } else {
            resp.put("status", recentErrors > 0 ? "DEGRADED" : "OK");
        }

        metrics.put("cpuLoadPercent", cpuLoad);
        metrics.put("memoryUsedPercent", memoryUsedPercent);
        metrics.put("uptimeSeconds", uptimeMs / 1000);
        metrics.put("avgLatencyMs", avgLatency);
        metrics.put("recentErrors", recentErrors);
        metrics.put("requestsTracked", totalRequests);

        resp.put("metrics", metrics);
        resp.put("timestamp", new Date().toString());

        return resp;
    }
}
