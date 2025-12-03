package com.api.controller;

import com.api.config.FailureConfig;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/control")
public class ControlController {

    // Added name="ms" to fix the reflection error
    @GetMapping("/delay")
    public Map<String, Object> setDelay(@RequestParam(name = "ms", defaultValue = "0") long ms) {
        FailureConfig.artificialDelayMs = Math.max(ms, 0);
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Artificial delay updated");
        resp.put("artificialDelayMs", FailureConfig.artificialDelayMs);
        return resp;
    }

    // Added name="percent"
    @GetMapping("/error-rate")
    public Map<String, Object> setErrorRate(@RequestParam(name = "percent", defaultValue = "0") int percent) {
        FailureConfig.errorRatePercent = Math.min(Math.max(percent, 0), 100);
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Error rate updated");
        resp.put("errorRatePercent", FailureConfig.errorRatePercent);
        return resp;
    }

    // Added name="enabled"
    @GetMapping("/db-failure")
    public Map<String, Object> toggleDbFailure(@RequestParam(name = "enabled", defaultValue = "true") boolean enabled) {
        FailureConfig.dbFailure = enabled;
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "DB failure flag updated");
        resp.put("dbFailure", FailureConfig.dbFailure);
        return resp;
    }

    // Added name="enabled"
    @GetMapping("/nullpointer")
    public Map<String, Object> toggleNullPointer(@RequestParam(name = "enabled", defaultValue = "true") boolean enabled) {
        FailureConfig.nullPointer = enabled;
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "NullPointer flag updated");
        resp.put("nullPointer", FailureConfig.nullPointer);
        return resp;
    }

    // Added name="enabled"
    @GetMapping("/timeout")
    public Map<String, Object> toggleTimeout(@RequestParam(name = "enabled", defaultValue = "true") boolean enabled) {
        FailureConfig.timeoutFailure = enabled;
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Timeout flag updated");
        resp.put("timeoutFailure", FailureConfig.timeoutFailure);
        return resp;
    }

    @GetMapping("/reset")
    public Map<String, Object> resetAll() {
        FailureConfig.reset();
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "All failure flags reset to normal");
        return resp;
    }
}