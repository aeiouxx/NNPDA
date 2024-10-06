package com.josefy.nnpda.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    private final long validityMilliseconds = 1000 * 60 * 60;
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public String generateToken(String username) {
        return generateToken(Map.of(), username);
    }
    public String generateToken(Map<String, Object> additionalClaims,
                                String username) {
        return Jwts.builder()
                .claims(additionalClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validityMilliseconds))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }
    public String generateToken(Map<String, Object> additionalClaims,
                                UserDetails userDetails) {
        return Jwts.builder()
                .claims(additionalClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validityMilliseconds))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    protected boolean isTokenExpired(String token) {
        try {
            return extractClaim(token, Claims::getExpiration).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
       final Claims claims = extractAllClaims(token);
       return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
