package org.antonioxocoy.cecommerce.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.antonioxocoy.cecommerce.models.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;

@Component
public class JWTTokenUtil {
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24hrs
    private static final Logger logger = LoggerFactory.getLogger(JWTTokenUtil.class);

    @Value("${security.jwt.secret}")
    private String SCRT_K;
    @Value("${app-name}")
    private String APP_NAME;

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setId(user.getId())
                .setIssuer(APP_NAME)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        // return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SCRT_K));
        SecretKey key = Keys.hmacShaKeyFor(SCRT_K.getBytes());
        return key;
    }

    public UsernamePasswordAuthenticationToken getEmailFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
            String email = claims.getSubject();
            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (JwtException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("JWT Token expired: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("JWT token is expired: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }
}
