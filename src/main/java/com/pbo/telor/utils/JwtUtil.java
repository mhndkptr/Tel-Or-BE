package com.pbo.telor.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.refresh-secret}")
    private String refreshSecret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private SecretKey secretKey;
    private SecretKey refreshSecretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecret.getBytes());
    }

    // ======== ACCESS TOKEN ========
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject, secretKey);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, SecretKey key) {
        final Claims claims = extractAllClaims(token, key);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, SecretKey key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    } catch (Exception e) {
        System.out.println("❌ validateToken error: " + e.getMessage());
        return false;
    }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration, secretKey);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails.getUsername(), expiration, secretKey);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails.getUsername(), refreshExpiration, refreshSecretKey);
    }

    private String createToken(Map<String, Object> claims, String subject, long expiration, SecretKey key) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ======== REFRESH TOKEN ONLY ========
    public String extractUsernameFromRefreshToken(String token) {
        return extractClaim(token, Claims::getSubject, refreshSecretKey);
    }

    public boolean validateRefreshToken(String token, UserDetails userDetails) {
        final String username = extractUsernameFromRefreshToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, refreshSecretKey));
    }

    private boolean isTokenExpired(String token, SecretKey key) {
        return extractExpiration(token, key).before(new Date());
    }

    private Date extractExpiration(String token, SecretKey key) {
        return extractClaim(token, Claims::getExpiration, key);
    }
}
