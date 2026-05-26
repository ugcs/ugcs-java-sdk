package com.ugcs.messaging;

import java.util.Objects;

public final class TaskDetails {
    public static final int HIGH_PRIORITY = Integer.MAX_VALUE;
    public static final int DEFAULT_PRIORITY = 0;

    public final Object isolation;
    public final int priority;

    public TaskDetails(Object isolation) {
        this(isolation, DEFAULT_PRIORITY);
    }

    public TaskDetails(Object isolation, int priority) {
        Objects.requireNonNull(isolation);

        this.isolation = isolation;
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        TaskDetails that = (TaskDetails)o;
        return priority == that.priority && Objects.equals(isolation, that.isolation);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(isolation);
        result = 31 * result + priority;
        return result;
    }
}
