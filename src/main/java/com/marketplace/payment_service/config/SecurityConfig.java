package com.marketplace.payment_service.config;

import com.marketplace.payment_service.security.JwtAuthFilter;
import com.marketplace.payment_service.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Securite du Payment Service.
 * Le service ne gere pas la connexion (c'est le role de user-service) :
 * il verifie seulement le JWT recu dans l'en-tete Authorization.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // active @PreAuthorize sur les controleurs
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // console H2

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(startsWith("/h2-console")).permitAll()
                        .requestMatchers(startsWith("/swagger-ui")).permitAll()
                        .requestMatchers(startsWith("/v3/api-docs")).permitAll()
                        .requestMatchers(startsWith("/api/dev/")).permitAll()      // jetons de test (profil standalone)
                        .requestMatchers(startsWith("/api/admin/")).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // Sans jeton valide : 401 (au lieu de 403 par defaut)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(new JwtAuthFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Aucun utilisateur n'est stocke ici (ils sont dans user-service).
     * Ce bean empeche Spring Boot de creer un utilisateur "user" avec un mot de passe aleatoire.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("Les utilisateurs sont geres par user-service");
        };
    }

    /** Compare le debut de l'URL (fonctionne aussi avec la console H2). */
    private static RequestMatcher startsWith(String prefix) {
        return request -> request.getRequestURI()
                .substring(request.getContextPath().length())
                .startsWith(prefix);
    }
}