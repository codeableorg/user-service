package com.ecommerce.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Security configuration for User Service.
 *
 * This configuration:
 * 1. Validates JWT tokens from Keycloak
 * 2. Protects all endpoints except public ones
 * 3. Supports reactive WebFlux (non-blocking)
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    return http
        // Disable CSRF for stateless JWT authentication
        .csrf(csrf -> csrf.disable())

        // Configure authorization rules
        .authorizeExchange(exchanges -> exchanges
            // Public endpoints (no authentication required)
            .pathMatchers("/api/v1/users/register").permitAll()
            .pathMatchers("/actuator/health").permitAll()

            // All other endpoints require authentication
            .anyExchange().authenticated())

        // Configure OAuth2 Resource Server for JWT validation
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> {
              // Keycloak will validate the JWT signature using JWKS endpoint
              // Default configuration will use the jwk-set-uri from application.yml
            }))

        .build();
  }
}