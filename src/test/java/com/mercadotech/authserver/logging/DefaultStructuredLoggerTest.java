package com.mercadotech.authserver.logging;

import com.mercadotech.authserver.context.ThreadContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultStructuredLoggerTest {

    private final StructuredLogger logger = new DefaultStructuredLogger(DefaultStructuredLoggerTest.class);

    @AfterEach
    void clearContext() {
        ThreadContext.clear();
    }

    @Test
    void storesCorrelationIdInThreadContext() {
        logger.info("msg", "cid");

        String value = ThreadContext.get("correlation_id", String.class);

        assertThat(value).isEqualTo("cid");
    }

    @Test
    void usesIdFromThreadContextWhenNullProvided() {
        ThreadContext.put("correlation_id", "foo");

        logger.warn("msg", null);

        assertThat(ThreadContext.get("correlation_id", String.class)).isEqualTo("foo");
    }

    @Test
    void generatesIdWhenNoneProvided() {
        logger.error("msg", null, null);

        String value = ThreadContext.get("correlation_id", String.class);

        assertThat(value).isNotNull();
    }
}
