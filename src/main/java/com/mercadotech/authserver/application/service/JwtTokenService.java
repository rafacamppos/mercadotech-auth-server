package com.mercadotech.authserver.application.service;

import com.mercadotech.authserver.domain.model.Credentials;
import com.mercadotech.authserver.domain.model.TokenData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import com.mercadotech.authserver.logging.DefaultStructuredLogger;
import com.mercadotech.authserver.logging.StructuredLogger;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenService implements TokenService {

    private static final long EXPIRATION_MILLIS = 3600000; // 1 hour
    private final StructuredLogger logger = new DefaultStructuredLogger(JwtTokenService.class);

    @Override
    public TokenData generateToken(Credentials credentials) {
        Key key = Keys.hmacShaKeyFor(credentials.getClientSecret().getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_MILLIS);
        String token = Jwts.builder()
                .setSubject(credentials.getClientId())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
        return TokenData.builder().token(token).build();
    }

    @Override
    public boolean validateToken(TokenData tokenData, Credentials credentials) {
        try {
            Key key = Keys.hmacShaKeyFor(credentials.getClientSecret().getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(tokenData.getToken())
                    .getBody();
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            logger.error("Erro ao validar o token", null, e);
            return false;
        }
    }
}
