package com.mercadotech.authserver.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter tokensIssuedCounter(MeterRegistry registry) {
        return Counter.builder("auth_tokens_issued")
                .description("Number of tokens issued")
                .register(registry);
    }

    @Bean
    public Timer loginTimer(MeterRegistry registry) {
        return Timer.builder("auth_login_latency")
                .description("Latency of login endpoint")
                .publishPercentileHistogram()
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
    }

    @Bean
    public Timer validateTimer(MeterRegistry registry) {
        return Timer.builder("auth_validate_latency")
                .description("Latency of token validation endpoint")
                .publishPercentileHistogram()
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
    }
}
