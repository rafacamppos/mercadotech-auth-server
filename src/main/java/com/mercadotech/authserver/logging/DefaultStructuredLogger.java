package com.mercadotech.authserver.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Default implementation of {@link StructuredLogger} that delegates to an SLF4J
 * {@link Logger} instance.
 */
public class DefaultStructuredLogger implements StructuredLogger {

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

    private void logWithCorrelationId(Runnable action, String correlationId) {
        if (correlationId != null) {
            MDC.put("correlation_id", correlationId);
        }
        try {
            action.run();
        } finally {
            if (correlationId != null) {
                MDC.remove("correlation_id");
            }
        }
    }
}
