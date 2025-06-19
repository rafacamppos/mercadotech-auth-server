package com.mercadotech.authserver.logging;

/**
 * Contract for writing structured log entries.
 */
public interface StructuredLogger {

    /**
     * Writes an INFO level entry.
     *
     * @param message       the message to log
     * @param correlationId optional correlation identifier
     */
    void info(String message, String correlationId);

    /**
     * Writes a WARN level entry.
     *
     * @param message       the message to log
     * @param correlationId optional correlation identifier
     */
    void warn(String message, String correlationId);

    /**
     * Writes an ERROR level entry.
     *
     * @param message       the message to log
     * @param correlationId optional correlation identifier
     * @param t             exception related to the error
     */
    void error(String message, String correlationId, Throwable t);
}
