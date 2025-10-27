package com.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class StatusController {

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();

        // Current time in readable format
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Simulated metrics
        Random random = new Random();
        double cpuLoad = 15 + (35 * random.nextDouble()); // Simulate 15–50% load
        double memoryUsage = 40 + (40 * random.nextDouble()); // Simulate 40–80%
        int activeConnections = random.nextInt(150) + 50; // Simulate 50–200 active connections

        // Uptime
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        long uptimeMs = rb.getUptime();
        long uptimeMin = uptimeMs / 60000;
        long uptimeHr = uptimeMin / 60;

        // Basic API metadata
        status.put("serverName", "Order Details API");
        status.put("environment", "Production");
        status.put("region", "us-east-1");
        status.put("status", "Healthy");
        status.put("timestamp", currentTime);

        // Metrics
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("uptimeHours", uptimeHr);
        metrics.put("uptimeMinutes", uptimeMin);
        metrics.put("cpuLoadPercent", String.format("%.2f", cpuLoad));
        metrics.put("memoryUsagePercent", String.format("%.2f", memoryUsage));
        metrics.put("activeConnections", activeConnections);

        status.put("metrics", metrics);

        // Last restart info
        status.put("lastRestart", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(System.currentTimeMillis() - uptimeMs)));

        // Response summary
        status.put("message", "Server is operating normally. All systems functional.");

        return status;
    }
}
