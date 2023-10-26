package com.cruxbackend.backend.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000"); // Replace with your frontend URL
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        
        // Set the Access-Control-Allow-Origin header
        config.addExposedHeader("Access-Control-Allow-Origin");
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
