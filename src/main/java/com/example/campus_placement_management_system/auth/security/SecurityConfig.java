package com.example.campus_placement_management_system.auth.security;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// NEW IMPORT: needed for .anonymous(AbstractHttpConfigurer::disable)
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // ======= NEW BLOCK START =======
                // Without this, Spring Security's default behavior returns 403
                // for BOTH "no token" and "wrong role" cases. This block makes
                // "no token" return 401, and "wrong role" return 403.
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // fires when there is NO valid authentication at all (no/invalid token)
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                            response.getWriter().write(
                                    "{\"error\":\"Unauthorized\",\"message\":\"Authentication  required\"}"
                            );
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            // fires when authenticated but role/permission is insufficient
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                            response.getWriter().write(
                                    "{\"error\":\"Forbidden\",\"message\":\"Access denied\"}"
                            );
                        })
                )
                // ======= NEW BLOCK END =======

                .authorizeHttpRequests(auth -> auth

                        // Public APIs
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/health",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        )
                        .permitAll()

                        // Admin APIs
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // Student APIs
                        .requestMatchers("/api/students/**")
                        .hasAnyRole("STUDENT", "ADMIN")

                        // Company APIs
                        .requestMatchers("/api/companies/**")
                        .hasAnyRole("COMPANY", "ADMIN")

                        // Everything else requires login
                        .anyRequest()
                        .authenticated()
                )

                // NEW LINE: disables Spring's default AnonymousAuthenticationFilter.
                // Without this, a request with NO token still gets an "anonymous"
                // Authentication object, which makes role checks fail with
                // AccessDeniedException (403) instead of AuthenticationException (401).
                .anonymous(AbstractHttpConfigurer::disable)

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }
}
