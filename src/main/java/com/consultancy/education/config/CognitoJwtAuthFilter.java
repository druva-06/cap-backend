package com.consultancy.education.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class CognitoJwtAuthFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;

    public CognitoJwtAuthFilter(String userPoolId, String region) {
        String jwkUrl = String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", region, userPoolId);
        this.jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkUrl).build();
        log.info("CognitoJwtAuthFilter initialized with JWK URL: {}", jwkUrl);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Jwt jwt = jwtDecoder.decode(token);
                log.debug("JWT successfully decoded for request: {}", request.getRequestURI());

                String tokenUse = (String) jwt.getClaims().get("token_use");
                if (!"access".equals(tokenUse)) {
                    log.warn("JWT token_use is not 'access'. Actual value: {}. Request URI: {}", tokenUse, request.getRequestURI());
                    throw new RuntimeException("Only access tokens are allowed");
                }

                List<String> groups = jwt.getClaimAsStringList("cognito:groups");
                if (groups == null || groups.isEmpty()) {
                    log.warn("No Cognito group found in JWT for user: {}", jwt.getClaimAsString("username"));
                    throw new RuntimeException("No group assigned to user");
                }
                String role = groups.get(0);
                log.info("User {} authenticated with role {}", jwt.getClaimAsString("username"), role);

                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        jwt.getClaimAsString("username"), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception ex) {
                log.error("JWT authentication failed: {}", ex.getMessage(), ex);
                SecurityContextHolder.clearContext();
            }
        } else {
            log.debug("No Authorization header found or not a Bearer token for request: {}", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }
}