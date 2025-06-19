package com.mercadotech.authserver.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import java.util.UUID;
import com.mercadotech.authserver.context.ThreadContext;

/**
 * Default implementation of {@link StructuredLogger} that delegates to an SLF4J
 * {@link Logger} instance.
 */
public class DefaultStructuredLogger implements StructuredLogger {

    static final String CORRELATION_ID_KEY = "correlation_id";
    private final Logger logger;

    public DefaultStructuredLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void info(String message, String correlationId) {
        logWithCorrelationId(() -> logger.info(message), correlationId);
    }

    @Override
    public void warn(String message, String correlationId) {
        logWithCorrelationId(() -> logger.warn(message), correlationId);
    }

    @Override
    public void error(String message, String correlationId, Throwable t) {
        logWithCorrelationId(() -> logger.error(message, t), correlationId);
    }

    /**
     * Executes the logging action with the correlation ID stored in the MDC.
     * A random UUID is generated when the provided ID is {@code null}.
     */
    private void logWithCorrelationId(Runnable action, String correlationId) {
        String cid = correlationId;
        if (cid == null) {
            cid = ThreadContext.get(CORRELATION_ID_KEY, String.class);
        }
        if (cid == null) {
            cid = UUID.randomUUID().toString();
        }
        ThreadContext.put(CORRELATION_ID_KEY, cid);
        MDC.put(CORRELATION_ID_KEY, cid);
        try {
            action.run();
        } finally {
            MDC.remove(CORRELATION_ID_KEY);
        }
    }
}
