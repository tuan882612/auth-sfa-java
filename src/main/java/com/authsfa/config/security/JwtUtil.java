package com.authsfa.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.UUID;

@Component
public class JwtUtil {
    private static final String SECRET = System.getenv("JWT_SECRET");
    private static final long EXPIRATION = 86400000; // 1 day

    public String generateToken(String userId, String email) {
        return Jwts.builder()
                .claim("user_id", userId)
                .claim("email", email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(UUID userId, String token) {
        Claims claims = extractAllClaims(token);
        return userId.equals(UUID.fromString(claims.get("user_id", String.class))) &&
                !claims.getExpiration().before(new Date(System.currentTimeMillis()));
    }
}
