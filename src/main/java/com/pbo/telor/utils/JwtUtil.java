package com.pbo.telor.utils;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtUtil {
    private static final String SECRET = System.getenv("JWT_SECRET");
    private static final String REFRESH_SECRET = System.getenv("JWT_REFRESH_SECRET");
    private static final long EXPIRATION = Long.parseLong(System.getenv("JWT_EXPIRATION"));
    private static final long REFRESH_EXPIRATION = Long.parseLong(System.getenv("JWT_REFRESH_EXPIRATION"));

    // ======== ACCESS TOKEN ========
    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject, SECRET);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String secret) {
        final Claims claims = extractAllClaims(token, secret);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token, String secret) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public static Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private static Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration, SECRET);
    }

    public static String generateAccessToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails.getUsername(), EXPIRATION, SECRET);
    }

    public static String generateRefreshToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails.getUsername(), REFRESH_EXPIRATION, REFRESH_SECRET);
    }

    private static String createToken(Map<String, Object> claims, String subject, long expiration, String secret) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // ======== REFRESH TOKEN ONLY ========
    public static String extractUsernameFromRefreshToken(String token) {
        return extractClaim(token, Claims::getSubject, REFRESH_SECRET);
    }

    public static boolean validateRefreshToken(String token, UserDetails userDetails) {
        final String username = extractUsernameFromRefreshToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, REFRESH_SECRET));
    }

    private static boolean isTokenExpired(String token, String secret) {
        return extractExpiration(token, secret).before(new Date());
    }

    private static Date extractExpiration(String token, String secret) {
        return extractClaim(token, Claims::getExpiration, secret);
    }
}
