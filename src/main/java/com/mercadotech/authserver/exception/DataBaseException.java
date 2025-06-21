package com.mercadotech.authserver.exception;

/**
 * Exceção para erros relacionados ao acesso a banco de dados.
 */
public class DataBaseException extends RuntimeException {

    public DataBaseException() {
        super();
    }

    public DataBaseException(String message) {
        super(message);
    }

    public DataBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
