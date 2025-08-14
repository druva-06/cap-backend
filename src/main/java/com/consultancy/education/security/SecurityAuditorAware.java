package com.consultancy.education.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component("auditorAware")
public class SecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext() != null
                ? SecurityContextHolder.getContext().getAuthentication() : null;

        String auditor = "system";
        if (auth != null && auth.isAuthenticated()) {
            if (auth instanceof JwtAuthenticationToken jwtAuth) {
                Jwt jwt = jwtAuth.getToken();
                String email = jwt.getClaimAsString("email");
                String username = jwt.getClaimAsString("cognito:username");
                auditor = (email != null && !email.isBlank()) ? email
                        : (username != null && !username.isBlank()) ? username
                        : auth.getName();
            } else {
                auditor = auth.getName();
            }
        }
        log.debug("Auditor resolved: {}", auditor);
        return Optional.ofNullable(auditor);
    }
}
