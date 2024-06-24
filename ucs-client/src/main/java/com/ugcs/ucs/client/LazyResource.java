package com.ugcs.ucs.client;

@FunctionalInterface
public interface LazyResource<T extends AutoCloseable>  {
    /**
     * The resource must be closed by the component that calls [allocate].
     */
    T allocate();
}
