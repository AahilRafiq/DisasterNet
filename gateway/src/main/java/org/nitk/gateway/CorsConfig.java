package org.nitk.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Allow your React frontend origin
        config.addAllowedOrigin("http://localhost:3000");

        // Allow credentials (cookies, JWT)
        config.setAllowCredentials(true);

        // Allow headers and methods
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        // Apply to all routes handled by the gateway
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}