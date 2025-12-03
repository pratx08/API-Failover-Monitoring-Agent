package com.api.controller;

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

        // uptime
        long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();

        // JVM memory
        long heapUsed = memoryBean.getHeapMemoryUsage().getUsed() / (1024 * 1024);
        long heapMax = memoryBean.getHeapMemoryUsage().getMax() / (1024 * 1024);
        double memoryUsedPercent = (heapUsed * 100.0) / heapMax;

        // CPU Load (if supported)
        double cpuLoad = 0;
        try {
            cpuLoad = (double) osBean.getClass().getMethod("getSystemCpuLoad").invoke(osBean) * 100;
        } catch (Exception ignored) {}

        metrics.put("cpuLoadPercent", cpuLoad);
        metrics.put("memoryUsedPercent", memoryUsedPercent);
        metrics.put("uptimeSeconds", uptimeMs / 1000);
        metrics.put("avgLatencyMs", ServerMetrics.getAverageLatency());
        metrics.put("recentErrors", ServerMetrics.getRecentErrorCount());
        metrics.put("requestsTracked", ServerMetrics.getTotalRequestsTracked());

        resp.put("status", "OK");
        resp.put("metrics", metrics);
        resp.put("timestamp", new Date().toString());

        return resp;
    }
}
