package com.mercadotech.authserver.client;

import com.mercadotech.authserver.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class ValidateTokenAspect {

    private final AuthClient authClient;

    public ValidateTokenAspect(AuthClient authClient) {
        this.authClient = authClient;
    }

    @Around("@annotation(com.mercadotech.authserver.client.ValidateToken)")
    public Object validate(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return pjp.proceed();
        }
        HttpServletRequest request = attrs.getRequest();
        String token = request.getHeader("Authorization");
        String clientId = request.getHeader("X-Client-Id");
        if (token == null || clientId == null) {
            throw new BusinessException("Missing authorization information");
        }
        boolean valid = authClient.validateToken(token, clientId);
        if (!valid) {
            throw new BusinessException("Invalid token");
        }
        return pjp.proceed();
    }
}
