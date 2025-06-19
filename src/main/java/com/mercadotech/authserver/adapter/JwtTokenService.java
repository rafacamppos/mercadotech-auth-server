package com.mercadotech.authserver.adapter;

import com.mercadotech.authserver.domain.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenService implements TokenService {

    private static final long EXPIRATION_MILLIS = 3600000; // 1 hour

    @Override
    public String generateToken(String clientId, String clientSecret) {
        Key key = Keys.hmacShaKeyFor(clientSecret.getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_MILLIS);
        return Jwts.builder()
                .setSubject(clientId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    @Override
    public boolean validateToken(String token, String clientSecret) {
        try {
            Key key = Keys.hmacShaKeyFor(clientSecret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
