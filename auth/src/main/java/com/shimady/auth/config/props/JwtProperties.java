package com.shimady.auth.config.props;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt.token")
public class JwtProperties {
    private final TokenProperties access = new TokenProperties();
    private final TokenProperties refresh = new TokenProperties();

    @Getter
    @Setter
    public static class TokenProperties {
        private Long expiration = 60 * 60 * 1000L;
        private String cookieName = "default_cookie";
        private String secret;
    }
}
