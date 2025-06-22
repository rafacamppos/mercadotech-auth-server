package com.mercadotech.authserver.client;

import com.mercadotech.authserver.adapter.server.dto.ValidateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class AuthClientTest {

    private RestTemplate restTemplate;
    private MockRestServiceServer server;
    private AuthClient client;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        server = MockRestServiceServer.createServer(restTemplate);
        client = new AuthClient(restTemplate, "http://auth");
    }

    @Test
    void validatesTokenViaRestCall() {
        server.expect(requestTo("http://auth/token/validate"))
                .andExpect(method(org.springframework.http.HttpMethod.POST))
                .andExpect(content().json("{\"token\":\"tok\",\"clientId\":\"id\"}"))
                .andRespond(withSuccess("{\"valid\":true}", MediaType.APPLICATION_JSON));

        boolean result = client.validateToken("tok", "id");

        assertThat(result).isTrue();
        server.verify();
    }

    @Test
    void returnsFalseWhenResponseNull() {
        server.expect(requestTo("http://auth/token/validate"))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        boolean result = client.validateToken("tok", "id");

        assertThat(result).isFalse();
        server.verify();
    }
}
