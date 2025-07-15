package com.consultancy.education.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Value("${aws.region}")
    private String region;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/auth/signup",
            "/auth/login",
            "/auth/forgotPassword/**",
            "/auth/resendVerificationCode/**",
            "/auth/confirmVerificationCode",
            "/auth/confirmForgotPassword",
            "/swagger-ui/**",
            "/v3/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(cognitoJwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CognitoJwtAuthFilter cognitoJwtAuthFilter() {
        return new CognitoJwtAuthFilter(userPoolId, region);
    }
}