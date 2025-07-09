package com.uca.parcialfinalncapas.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Clave secreta para firmar los tokens JWT (configurada en application.properties)
    @Value("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970")
    private String secretKey;

    // Tiempo de expiración del token en milisegundos (configurado en application.properties)
    @Value("86400000")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)                                    // Claims adicionales
                .setSubject(userDetails.getUsername())                     // Subject (email del usuario)
                .setIssuedAt(new Date(System.currentTimeMillis()))        // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Fecha de expiración
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)      // Firma con algoritmo HS256
                .compact();                                               // Construir el token
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())    // Configurar la clave de firma
                .build()
                .parseClaimsJws(token)             // Parsear el token
                .getBody();                        // Obtener los claims
    }

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
