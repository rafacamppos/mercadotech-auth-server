package com.mercadotech.authserver.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TokenValidationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate authRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthClient authClient(RestTemplate restTemplate,
                                 @Value("${auth.server.url:http://localhost:8080}") String url) {
        return new AuthClient(restTemplate, url);
    }

    @Bean
    @ConditionalOnMissingBean
    public ValidateTokenAspect validateTokenAspect(AuthClient client) {
        return new ValidateTokenAspect(client);
    }
}
