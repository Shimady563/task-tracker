package com.shimady.tracker.repository;

import com.shimady.tracker.config.props.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;

@Slf4j
@Repository
public class JwtProvider {
    private final SecretKey accessSecret;

    public JwtProvider(JwtProperties jwtProperties) {
        this.accessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getAccess().getSecret()));
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, accessSecret);
    }

    private boolean validateToken(String token, SecretKey secret) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Jwt token expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("Jwt token unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Jwt token malformed: {}", e.getMessage());
        } catch (SignatureException e) {
            log.warn("Jwt token has wrong signature: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid jwt token: {}", e.getMessage());
        }
        return false;
    }

    public Claims getClaimsFromAccessToken(String token) {
        return getClaimsFromToken(token, accessSecret);
    }

    private Claims getClaimsFromToken(String token, SecretKey secret) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
