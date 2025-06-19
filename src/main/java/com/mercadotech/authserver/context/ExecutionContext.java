package com.mercadotech.authserver.context;

/**
 * Simple contract for storing and retrieving values during the execution of a
 * single thread.
 */
public interface ExecutionContext {

    /**
     * Stores a value associated with the given key.
     *
     * @param key   identifier of the value
     * @param value value to store
     */
    void put(String key, Object value);

    /**
     * Retrieves the value associated with the given key or {@code null} when
     * none is present.
     *
     * @param key identifier of the value
     * @return stored value or {@code null}
     */
    Object get(String key);

    /**
     * Removes a value associated with the given key.
     *
     * @param key identifier of the value
     */
    void remove(String key);

    /**
     * Clears all stored values from this context.
     */
    void clear();

    /**
     * Returns a copy of all stored values.
     *
     * @return map containing the current context values
     */
    java.util.Map<String, Object> values();
}
