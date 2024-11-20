package com.josefy.nnpda.config;

import com.josefy.nnpda.infrastructure.security.ApiKeyAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(1)
public class ApiKeySecurityConfig {
    private final ApiKeyAuthenticationFilter apiKeyAuthFilter;

    public static AntPathRequestMatcher TARGET_MATCHER = new AntPathRequestMatcher("api/devices/measurements/**");

    @Bean
    public SecurityFilterChain deviceSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(TARGET_MATCHER)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                                auth.requestMatchers(TARGET_MATCHER).authenticated()
                                        .anyRequest().permitAll()
                        )
                .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}