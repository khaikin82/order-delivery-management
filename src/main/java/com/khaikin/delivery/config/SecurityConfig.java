package com.khaikin.delivery.config;

import com.khaikin.delivery.entity.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Cho phép truy cập không cần token
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/api/**"
                        ).permitAll()

                        // User
//                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasRole(Role.ADMIN.name())
//                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole(Role.ADMIN.name())
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole(Role.ADMIN.name())
//                        .requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole(Role.ADMIN.name())

                        // Order
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders").hasRole(Role.CUSTOMER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/my").hasRole(Role.CUSTOMER.name())
//                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").hasRole(Role.ADMIN.name())
//                        .requestMatchers(HttpMethod.PUT, "/api/v1/orders/**").hasRole(Role.ADMIN.name())
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/orders/**").hasRole(Role.ADMIN.name())
//                        .requestMatchers("/api/v1/orders/**/assign").hasRole(Role.ADMIN.name())
                        .requestMatchers("/api/v1/orders/**/status").hasRole(Role.DELIVERY_STAFF.name())

                        // Tracking
                        .requestMatchers("/api/v1/tracking/**").hasAnyRole(Role.CUSTOMER.name(), Role.ADMIN.name())
                        .requestMatchers("/api/v1/**").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Dùng NimbusJwtDecoder với secret key
        if (jwtSecret.isBlank()) {
            throw new IllegalArgumentException("JWT secret must not be empty");
        }
        byte[] secretKey = jwtSecret.getBytes();

        return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(secretKey, "HmacSHA256")).build();
    }

    private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        return jwt -> {
            Collection<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + jwt.getClaimAsString("role"))
            );
            return new JwtAuthenticationToken(jwt, authorities);
        };
    }
}

