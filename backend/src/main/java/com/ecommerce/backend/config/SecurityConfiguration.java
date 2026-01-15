package com.ecommerce.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults()) // CORS yapılandırmasını aşağıda tanımladığımız bean'e bağladık
            .csrf(csrf -> csrf.disable())
            .securityContext(sc -> sc.disable())
            .sessionManagement(sm -> sm.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // İzin verilen kökenler (Origins)
        configuration.setAllowedOrigins(Arrays.asList(
            "http://demirayhidrolik.com",
            "http://www.demirayhidrolik.com",
            "http://62.72.21.81:81"
        ));
        
        // İzin verilen HTTP Metodları
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // İzin verilen Headerlar
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "X-Requested-With"));
        
        // Tarayıcının cevabı önbelleğe alması için (Preflight isteği her seferinde atılmasın diye)
        configuration.setMaxAge(3600L);
        
        // Credentials (Auth headerları için gerekebilir)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
