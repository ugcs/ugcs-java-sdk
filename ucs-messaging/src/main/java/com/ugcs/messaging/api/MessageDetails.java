package com.ugcs.messaging.api;

import java.util.Objects;

public final class MessageDetails {
    public static final int HIGH_PRIORITY = Integer.MAX_VALUE;
    public static final int DEFAULT_PRIORITY = 0;

    public final Object isolation;
    public final int priority;

    public MessageDetails(Object isolation) {
        this(isolation, DEFAULT_PRIORITY);
    }

    public MessageDetails(Object isolation, int priority) {
        if (isolation == null) {
            throw new NullPointerException("isolation cannot be null");
        }

        this.isolation = isolation;
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        MessageDetails that = (MessageDetails)o;
        return priority == that.priority && Objects.equals(isolation, that.isolation);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(isolation);
        result = 31 * result + priority;
        return result;
    }
}
