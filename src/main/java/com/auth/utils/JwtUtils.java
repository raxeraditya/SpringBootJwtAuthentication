package com.auth.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    // Use Keys to generate a secure key (replace "secretKey" with a strong secret)
    private final SecretKey jwtSecret = Keys.hmacShaKeyFor("your-strong-secret-key-here-1234567890".getBytes());

    private final long jwtExpirationMs = 86400000; // 1 day

    // Generate JWT token
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(jwtSecret) // Use SecretKey directly
                .compact();
    }

    // Validate JWT token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(jwtSecret) // Use verifyWith() instead of setSigningKey()
                    .build()
                    .parseSignedClaims(authToken); // Use parseSignedClaims() instead of parseClaimsJws()
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Get username from JWT token
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}