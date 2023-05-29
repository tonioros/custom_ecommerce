package org.antonioxocoy.cecommerce.security.jwt.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.antonioxocoy.cecommerce.models.entity.User;
import org.antonioxocoy.cecommerce.security.encription.EncryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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

    @Autowired
    private EncryptService es;

    public String generateAccessToken(User user) throws InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException,
            InvalidKeySpecException, InvalidKeyException {
        String emailEnc = es.encrypt(user.getEmail());
        String idEnc = es.encrypt(user.getId());
        return Jwts.builder()
                .setSubject(emailEnc)
                .setId(idEnc)
                .setIssuer(APP_NAME)
                .claim("type", user.getType())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SCRT_K.getBytes());
    }

    public UsernamePasswordAuthenticationToken getEmailFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
            String email = es.decrypt(claims.getSubject());
            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (JwtException | InvalidAlgorithmParameterException | NoSuchPaddingException |
                 IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeySpecException |
                 InvalidKeyException ex) {
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
