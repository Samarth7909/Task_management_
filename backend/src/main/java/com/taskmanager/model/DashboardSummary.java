package com.taskmanager.model;

public class DashboardSummary {

    private long totalTasks;
    private long pendingTasks;
    private long completedTasks;

    public DashboardSummary(long totalTasks, long pendingTasks, long completedTasks) {
        this.totalTasks = totalTasks;
        this.pendingTasks = pendingTasks;
        this.completedTasks = completedTasks;
    }

    public long getTotalTasks() { return totalTasks; }
    public long getPendingTasks() { return pendingTasks; }
    public long getCompletedTasks() { return completedTasks; }
}
