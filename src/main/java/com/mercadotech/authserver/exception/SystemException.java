package com.mercadotech.authserver.exception;

/**
 * Exceção genérica para falhas inesperadas no sistema.
 */
public class SystemException extends RuntimeException {

    public SystemException() {
        super();
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
