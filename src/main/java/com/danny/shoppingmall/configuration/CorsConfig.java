package com.danny.shoppingmall.configuration;

import com.danny.shoppingmall.jwt.dto.JwtProperties;
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
        config.addAllowedOriginPattern("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.addExposedHeader(JwtProperties.ACCESS_HEADER_STRING);
        config.addExposedHeader(JwtProperties.REFRESH_HEADER_STRING);
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
