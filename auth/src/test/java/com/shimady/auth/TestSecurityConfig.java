package com.shimady.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shimady.auth.config.props.AuthProperties;
import com.shimady.auth.config.props.JwtProperties;
import com.shimady.auth.security.filter.JwtFilter;
import com.shimady.auth.repository.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@TestConfiguration
@RequiredArgsConstructor
public class TestSecurityConfig {
    private final AuthProperties authProperties;
    private final JwtProperties jwtProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(authProperties.getAllowedOrigins());
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    auth.requestMatchers(authProperties.getWhitelist().toArray(String[]::new)).permitAll();
                    auth.anyRequest().authenticated();
                })
                .addFilterAt(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Primary
    public JwtFilter jwtFilter() {
        return new TestJwtFilter(authProperties, jwtProperties, jwtProvider(), new ObjectMapper());
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(jwtProperties);
    }


    // overriding the jwt filter to turn it off
    // because every other method didn't work
    private static class TestJwtFilter extends JwtFilter {
        public TestJwtFilter(AuthProperties authProperties, JwtProperties jwtProperties, JwtProvider jwtProvider, ObjectMapper objectMapper) {
            super(authProperties, jwtProperties, jwtProvider, objectMapper);
        }

        @Override
        protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
            return true;
        }
    }
}