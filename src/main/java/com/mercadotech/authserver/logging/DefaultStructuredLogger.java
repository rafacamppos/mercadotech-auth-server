package com.mercadotech.authserver.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import com.mercadotech.authserver.context.ExecutionContext;
import java.util.UUID;

/**
 * Default implementation of {@link StructuredLogger} that delegates to an SLF4J
 * {@link Logger} instance.
 */
public class DefaultStructuredLogger implements StructuredLogger {

    private final Logger logger;
    private final ExecutionContext executionContext;

    public DefaultStructuredLogger(Class<?> clazz) {
        this(clazz, null);
    }

    public DefaultStructuredLogger(Class<?> clazz, ExecutionContext context) {
        this.logger = LoggerFactory.getLogger(clazz);
        this.executionContext = context;
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
        String cid = correlationId != null ? correlationId : UUID.randomUUID().toString();
        MDC.put("correlation_id", cid);
        if (executionContext != null && !executionContext.values().isEmpty()) {
            MDC.put("context", executionContext.values().toString());
        }
        try {
            action.run();
        } finally {
            if (executionContext != null) {
                MDC.remove("context");
            }
            MDC.remove("correlation_id");
        }
    }
}
