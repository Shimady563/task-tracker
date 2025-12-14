package com.shimady.tracker.utils;


import com.shimady.tracker.model.JwtAuthentication;
import com.shimady.tracker.model.UserRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class JwtUtils {
    public static JwtAuthentication generateAuthentication(Claims claims) {
        JwtAuthentication auth = new JwtAuthentication();
        auth.setEmail(claims.getSubject());
        auth.setRole(UserRole.valueOf(claims.get("role", String.class)));
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
}
