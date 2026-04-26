package com.compute.rental.scheduler;

public record SchedulerRunResult(
        String taskName,
        int totalCount,
        int successCount,
        int failCount,
        String status,
        String errorMessage
) {
}
