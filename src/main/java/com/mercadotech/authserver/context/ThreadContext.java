package com.mercadotech.authserver.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores variables scoped to the current thread using {@link ThreadLocal}.
 */
public final class ThreadContext {

    private static final ThreadLocal<Map<String, Object>> CONTEXT =
            ThreadLocal.withInitial(HashMap::new);

    private ThreadContext() {
    }

    /**
     * Stores a value in the context for the current thread.
     *
     * @param key   key under which the value will be stored
     * @param value value to store
     */
    public static void put(String key, Object value) {
        CONTEXT.get().put(key, value);
    }

    /**
     * Retrieves a value from the context, casting it to the requested type.
     * Returns {@code null} when the key is not present.
     *
     * @param key  key of the value
     * @param type expected class of the value
     * @param <T>  type parameter
     * @return stored value or {@code null}
     * @throws ClassCastException when the stored value is not of the requested type
     */
    public static <T> T get(String key, Class<T> type) {
        Object value = CONTEXT.get().get(key);
        if (value == null) {
            return null;
        }
        return type.cast(value);
    }

    /**
     * Removes the value associated with the given key for the current thread.
     */
    public static void remove(String key) {
        CONTEXT.get().remove(key);
    }

    /**
     * Clears all values for the current thread.
     */
    public static void clear() {
        CONTEXT.remove();
    }
}
