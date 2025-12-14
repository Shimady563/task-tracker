package com.shimady.auth.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum UserRole implements GrantedAuthority {
    ROLE_USER("user"),
    ;

    private final String value;

    @Override
    public String getAuthority() {
        return name();
    }
}
