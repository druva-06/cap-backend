package com.meritcap.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration.
 * CORS is handled centrally via SecurityConfig CorsConfigurationSource bean.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // CORS configured in SecurityConfig.corsConfigurationSource()
}
