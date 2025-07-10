package com.amazingshop.personal.userservice.config;

import com.amazingshop.personal.userservice.models.Role;
import com.amazingshop.personal.userservice.services.PeopleDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final PeopleDetailsService peopleDetailService;
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(PeopleDetailsService peopleDetailService, JwtFilter jwtFilter) {
        this.peopleDetailService = peopleDetailService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(peopleDetailService) // Добавляю свой DetailService
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/helloAdmin").hasRole(Role.ADMIN.toString())
                        .requestMatchers("/auth/**", "users/me").permitAll()
                        .anyRequest().hasAnyRole(Role.USER.toString(), Role.ADMIN.toString()))
                .logout(log -> log
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"))
                // jwt setting
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
