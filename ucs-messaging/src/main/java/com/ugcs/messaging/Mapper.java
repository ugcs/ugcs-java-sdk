package com.ugcs.messaging;

public interface Mapper<T, R> {

	R map(T source) throws Exception;
}
