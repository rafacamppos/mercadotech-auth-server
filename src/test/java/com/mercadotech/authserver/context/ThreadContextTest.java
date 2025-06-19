package com.mercadotech.authserver.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ThreadContextTest {

    @AfterEach
    void tearDown() {
        ThreadContext.clear();
    }

    @Test
    void storesAndRetrievesValueInSameThread() {
        ThreadContext.put("foo", "bar");

        String value = ThreadContext.get("foo", String.class);

        assertThat(value).isEqualTo("bar");
    }

    @Test
    void valuesAreThreadLocal() throws Exception {
        ThreadContext.put("foo", "bar");
        final String[] fromOtherThread = new String[1];
        Thread t = new Thread(() -> fromOtherThread[0] = ThreadContext.get("foo", String.class));
        t.start();
        t.join();

        assertThat(fromOtherThread[0]).isNull();
        assertThat(ThreadContext.get("foo", String.class)).isEqualTo("bar");
    }
}
