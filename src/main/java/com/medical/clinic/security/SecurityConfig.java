package com.medical.clinic.security;

import com.medical.clinic.security.oauth2.CustomOAuth2UserService;
import com.medical.clinic.security.oauth2.OAuth2FailureHandler;
import com.medical.clinic.security.oauth2.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    public SecurityConfig(JwtFilter jwtFilter,
                          CorsConfigurationSource corsConfigurationSource,
                          CustomOAuth2UserService customOAuth2UserService,
                          OAuth2SuccessHandler oAuth2SuccessHandler,
                          OAuth2FailureHandler oAuth2FailureHandler) {
        this.jwtFilter = jwtFilter;
        this.corsConfigurationSource = corsConfigurationSource;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session ->
                        // OAuth2 needs a session during the redirect handshake
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(auth -> auth
                        // ── Public auth endpoints ──────────────────────────
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/payments/payhere/notify").permitAll()

                        // ── OAuth2 flow paths — MUST be permitted ──────────
                        // authorization endpoint  → /api/auth/oauth2/authorize/google
                        // Spring's internal login  → /login/oauth2/**
                        // callback endpoint        → /api/auth/oauth2/callback/*
                        .requestMatchers(
                                "/api/auth/oauth2/**",
                                "/login/oauth2/**",
                                "/oauth2/**"
                        ).permitAll()

                        // ── Swagger ────────────────────────────────────────
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**"
                        ).permitAll()

                        // ── Authenticated user self-service ────────────────
                        .requestMatchers(
                                "/api/user/me",
                                "/api/user/update",
                                "/api/user/logout",
                                "/api/user/delete",
                                "/api/user/change-password"
                        ).authenticated()

                        // ── Role-based ─────────────────────────────────────
                        .requestMatchers("/api/user/**").hasRole("ADMIN")
                        .requestMatchers("/api/reception/**")
                        .hasAnyRole("RECEPTIONIST", "ADMIN")
                        .requestMatchers("/api/appointments/book", "/api/appointments/my")
                        .hasRole("PATIENT")
                        .requestMatchers("/api/payments/payhere/checkout")
                        .hasRole("PATIENT")
                        .requestMatchers(
                                "/api/doctors/**",
                                "/api/appointments/**",
                                "/api/departments/**",
                                "/api/patients/**",
                                "/api/medical-reports/**",
                                "/api/pharmacy/**",
                                "/api/files/**",
                                "/api/bills/**",
                                "/api/payments/**"
                        ).authenticated()

                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        // Frontend redirects browser to this URL to kick off the flow
                        .authorizationEndpoint(endpoint ->
                                endpoint.baseUri("/api/auth/oauth2/authorize")
                        )
                        // Must match spring.security.oauth2.client.registration.google.redirect-uri in yml
                        .redirectionEndpoint(endpoint ->
                                endpoint.baseUri("/api/auth/oauth2/callback/*")
                        )
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}