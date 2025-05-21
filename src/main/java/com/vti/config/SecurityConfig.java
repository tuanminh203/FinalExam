package com.vti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF cho REST API nếu bạn không dùng session
                .authorizeHttpRequests(authorize -> authorize // Gộp tất cả requestMatchers vào một khối
                        .requestMatchers("/api/auth/**").permitAll()// Cho phép tất cả các đường dẫn dưới /api/auth
                        .requestMatchers("/api/courses/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/lessons/**").hasRole("ADMIN")

                        // Cho phép truy cập Swagger UI và các tài nguyên liên quan
                        .requestMatchers("/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**", // Đây là endpoint OpenAPI JSON
                                "/webjars/**").permitAll() // Thường cần cho tài nguyên UI
                        .anyRequest().authenticated() // Mọi request khác yêu cầu xác thực
                );
//        .httpBasic(Customizer.withDefaults()); // Bỏ qua nếu không dùng Basic Auth

        return http.httpBasic().and().build();
    }
}