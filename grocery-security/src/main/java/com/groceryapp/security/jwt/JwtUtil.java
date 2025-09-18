package com.groceryapp.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT Utility class for token generation and validation
 */
@Slf4j
@Component
public class JwtUtil {
    
    @Value("${jwt.secret:MyVerySecureJwtSecretKeyThatIs256BitsLongForHMACAlgorithm}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;
    
    private volatile SecretKey signingKey;
    
    private SecretKey getSigningKey() {
        if (signingKey == null) {
            synchronized (this) {
                if (signingKey == null) {
                    // Ensure the key is at least 256 bits (32 bytes) for HS256
                    byte[] keyBytes;
                    
                    if (jwtSecret.length() < 32) {
                        // If the secret is too short, use a default secure key
                        log.warn("JWT secret is too short ({}), using default secure key", jwtSecret.length());
                        String defaultKey = "MyVerySecureJwtSecretKeyThatIs256BitsLongForHMACAlgorithm";
                        keyBytes = defaultKey.getBytes(StandardCharsets.UTF_8);
                    } else {
                        keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
                    }
                    
                    // Ensure we have at least 32 bytes for HS256
                    if (keyBytes.length < 32) {
                        byte[] paddedKey = new byte[32];
                        System.arraycopy(keyBytes, 0, paddedKey, 0, keyBytes.length);
                        // Fill remaining bytes with a pattern
                        for (int i = keyBytes.length; i < 32; i++) {
                            paddedKey[i] = (byte) (i % 256);
                        }
                        keyBytes = paddedKey;
                    }
                    
                    this.signingKey = Keys.hmacShaKeyFor(keyBytes);
                    log.info("JWT utility initialized with secure key of {} bits", keyBytes.length * 8);
                }
            }
        }
        return signingKey;
    }
    
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
    
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }
}