package com.mercadotech.authserver.context;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link ExecutionContext} implementation backed by a {@link ThreadLocal} map.
 */
public class ThreadLocalExecutionContext implements ExecutionContext {

    private static final ThreadLocal<Map<String, Object>> CONTEXT =
            ThreadLocal.withInitial(HashMap::new);

    @Override
    public void put(String key, Object value) {
        CONTEXT.get().put(key, value);
    }

    @Override
    public Object get(String key) {
        return CONTEXT.get().get(key);
    }

    @Override
    public void remove(String key) {
        CONTEXT.get().remove(key);
    }

    @Override
    public void clear() {
        CONTEXT.get().clear();
    }

    @Override
    public Map<String, Object> values() {
        return new HashMap<>(CONTEXT.get());
    }
}
