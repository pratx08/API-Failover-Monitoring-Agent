package com.api.controller;

import com.api.config.FailureConfig;
import com.api.metrics.ServerMetrics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api") // Matches the frontend's expected path structure
public class ResourceController {

    // Frontend calls: axios.get(`${baseUrl}/api/resource`)
    @GetMapping("/resource")
    public Map<String, Object> getResource() {

        long start = System.currentTimeMillis();
        Map<String, Object> response = new HashMap<>();
        Random random = new Random();
        boolean errorOccurred = false;

        try {
            // --- 1. FAILURE INJECTION LOGIC (Controlled by React UI) ---

            // Latency Slider
            if (FailureConfig.artificialDelayMs > 0) {
                Thread.sleep(FailureConfig.artificialDelayMs);
            }

            // Timeout Button
            if (FailureConfig.timeoutFailure) {
                Thread.sleep(5000); // Sleep longer than typical client timeout
            }

            // DB Failure Button
            if (FailureConfig.dbFailure) {
                throw new RuntimeException("Simulated Database Connection Failure");
            }

            // Null Pointer Button
            if (FailureConfig.nullPointer) {
                throw new NullPointerException("Simulated Null Pointer Exception");
            }

            // Error Rate Slider
            if (FailureConfig.errorRatePercent > 0 &&
                    random.nextInt(100) < FailureConfig.errorRatePercent) {
                errorOccurred = true;
                response.put("status", "ERROR");
                response.put("error", "Simulated Random Failure");
                response.put("timestamp", now());
                // Return early so we track it as an error
                return response;
            }

            // --- 2. SUCCESS LOGIC (Normal Payload) ---

            List<String> data = Arrays.asList("Packet-1", "Packet-2", "Packet-3");

            response.put("status", "SUCCESS");
            response.put("server", "Java Backend Node");
            response.put("data", data);
            response.put("timestamp", now());

            return response;

        } catch (Exception ex) {
            errorOccurred = true;
            response.put("status", "ERROR");
            response.put("error", ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return response;

        } finally {
            // --- 3. METRICS TRACKING ---
            long latency = System.currentTimeMillis() - start;
            ServerMetrics.recordLatency(latency);
            ServerMetrics.recordError(errorOccurred);
        }
    }

    private String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}