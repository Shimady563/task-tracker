package com.shimady.auth.utils;

import com.shimady.auth.model.JwtAuthentication;
import com.shimady.auth.model.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;

@UtilityClass
public class JwtUtils {
    public static JwtAuthentication generateAuthentication(Claims claims) {
        JwtAuthentication auth = new JwtAuthentication();
        auth.setEmail(claims.getSubject());
        auth.setRole(Role.valueOf(claims.get("role", String.class)));
        return auth;
    }

    public static String getTokenFromCookies(Cookie[] cookies, String tokenName) {
        if (cookies == null) {
            return null;
        }
        Cookie tokenCookie = Arrays.stream(cookies)
                .filter(c -> c.getName().equals(tokenName))
                .findFirst()
                .orElse(null);
        return tokenCookie == null ? null : tokenCookie.getValue();
    }

    public static String createTokenCookie(String tokenName, String tokenValue, Long maxAgeMs) {
        return createCookie(tokenName, tokenValue, maxAgeMs).toString();
    }

    public static String removeTokenCookie(String tokenName) {
        return createCookie(tokenName, "", 0L).toString();
    }

    private static ResponseCookie createCookie(String tokenName, String tokenValue, Long maxAgeMs) {
        return ResponseCookie.from(tokenName, tokenValue)
                .httpOnly(true)
                .secure(true)
                .maxAge(maxAgeMs / 1000L)
                .path("/api/v1")
                .build();
    }
}
