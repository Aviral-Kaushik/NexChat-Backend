package com.aviral.nexchat.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import com.aviral.nexchat.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
public class JWTService {

    private final String secret = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 31556952000L))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Date exp = Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();

            return exp.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
