package com.mercadotech.authserver.exception;

/**
 * Exceção genérica para falhas inesperadas no sistema.
 */
public class SistemaException extends RuntimeException {

    public SistemaException() {
        super();
    }

    public SistemaException(String message) {
        super(message);
    }

    public SistemaException(String message, Throwable cause) {
        super(message, cause);
    }
}
