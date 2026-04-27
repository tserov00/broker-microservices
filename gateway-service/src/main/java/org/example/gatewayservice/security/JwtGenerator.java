package org.example.gatewayservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtGenerator {

    private final Key key = Keys.hmacShaKeyFor(
            Base64.getDecoder().decode("ZzYJ/6d54kjZQM2z0a02xMfHyZAeDq+krlmL8DAnR5KiyeLQfEvj19ZgkLmXX7WcZd3L5XsCNN04B+X0jhhKbg==")
    );

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new JwtException("Invalid JWT token");
        }
    }

    public Key getSigningKey() {
        return key;
    }
}
