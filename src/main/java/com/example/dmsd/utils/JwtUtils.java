package com.example.dmsd.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret1;
//    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512, secret1);
//    String secret = Encoders.BASE64.encode(key.getEncoded());
    @Value("${jwt.expiration.ms}")
    private long expirationMs;

    public String generateToken(String email) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", email);
        claims.put("created", new Date());

        Date expiration = new Date(System.currentTimeMillis() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret1)
                .compact();
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret1)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret1).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("53: JwtUtils : "+e.getMessage());
            return false;
        }
    }
}
