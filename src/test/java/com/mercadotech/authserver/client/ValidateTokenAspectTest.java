package com.mercadotech.authserver.client;

import com.mercadotech.authserver.exception.BusinessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.aop.framework.ProxyFactory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ValidateTokenAspectTest {

    private AuthClient authClient;
    private TestService proxy;

    interface TestService { void call(); }

    @AfterEach
    void clearContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @BeforeEach
    void setUp() {
        authClient = Mockito.mock(AuthClient.class);
        ValidateTokenAspect aspect = new ValidateTokenAspect(authClient);
        ProxyFactory factory = new ProxyFactory(new TestServiceImpl());
        factory.addAspect(aspect);
        proxy = (TestService) factory.getProxy();
    }

    @Test
    void proceedsWhenTokenValid() {
        when(authClient.validateToken("tok", "id")).thenReturn(true);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "tok");
        request.addHeader("X-Client-Id", "id");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        proxy.call();

        verify(authClient).validateToken("tok", "id");
    }

    @Test
    void throwsWhenInvalid() {
        when(authClient.validateToken("tok", "id")).thenReturn(false);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "tok");
        request.addHeader("X-Client-Id", "id");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        assertThatThrownBy(() -> proxy.call()).isInstanceOf(BusinessException.class);
        verify(authClient).validateToken("tok", "id");
    }

    @Test
    void throwsWhenHeadersMissing() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        assertThatThrownBy(() -> proxy.call()).isInstanceOf(BusinessException.class);
        verifyNoInteractions(authClient);
    }

    static class TestServiceImpl implements TestService {
        @Override
        @ValidateToken
        public void call() { }
    }
}
