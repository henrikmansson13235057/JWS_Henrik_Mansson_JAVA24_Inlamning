package com.fulkoping.uthyrning.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/h2-console/**",
                                "/api/**"
                        ).permitAll()
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }
}


