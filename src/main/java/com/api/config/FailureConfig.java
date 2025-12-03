package com.api.config;

public class FailureConfig {

    // Artificial delay (in ms) added to each request
    public static volatile long artificialDelayMs = 0;

    // Percentage of requests that should fail (0–100)
    public static volatile int errorRatePercent = 0;

    // Flags for specific failure types
    public static volatile boolean dbFailure = false;
    public static volatile boolean nullPointer = false;
    public static volatile boolean timeoutFailure = false;

    public static void reset() {
        artificialDelayMs = 0;
        errorRatePercent = 0;
        dbFailure = false;
        nullPointer = false;
        timeoutFailure = false;
    }
}
