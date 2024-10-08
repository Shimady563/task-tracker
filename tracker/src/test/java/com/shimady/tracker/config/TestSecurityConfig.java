package com.shimady.tracker.config;

import com.shimady.tracker.handler.CustomAuthenticationFailureHandler;
import com.shimady.tracker.handler.CustomLogoutSuccessHandler;
import com.shimady.tracker.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class TestSecurityConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/users/signup").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> {
                    form.loginPage("/users/login");
                    form.defaultSuccessUrl("/users/me");
                    form.failureHandler(authenticationFailureHandler());
                })
                .logout(logout -> {
                    logout.logoutUrl("/users/logout");
                    logout.logoutSuccessHandler(logoutSuccessHandler());
                })
                .build();
    }
}
