package com.mercadotech.authserver.exception;

import com.mercadotech.authserver.logging.DefaultStructuredLogger;
import com.mercadotech.authserver.logging.StructuredLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final StructuredLogger logger = new DefaultStructuredLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusiness(BusinessException ex) {
        logger.warn(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<String> handleDatabase(DataBaseException ex) {
        logger.error(ex.getMessage(), null, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<String> handleSistema(SystemException ex) {
        logger.error("Unexpected system error", null, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleDefault(Exception ex) {
        logger.error("Unhandled exception", null, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
    }
}
