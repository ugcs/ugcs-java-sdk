package com.ugcs.messaging;

public interface TaskMapper {

	TaskDetails map(Runnable runnable);
}
