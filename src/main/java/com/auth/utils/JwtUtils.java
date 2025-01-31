package com.auth.utils;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import com.auth.exception.JwtTokenException;
import io.jsonwebtoken.*;

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
            throw new JwtTokenException("Invalid or expired token.");
        }
    }

    // Get username from JWT token
    public String getUserNameFromJwtToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(jwtSecret)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenException("Token expired! Please login again.");
        } catch (@SuppressWarnings("deprecation") UnsupportedJwtException | MalformedJwtException
                | SignatureException ex) {
            throw new JwtTokenException("Invalid token! Authentication failed.");
        } catch (Exception ex) {
            throw new JwtTokenException("Authentication error: " + ex.getMessage());
        }
    }
}