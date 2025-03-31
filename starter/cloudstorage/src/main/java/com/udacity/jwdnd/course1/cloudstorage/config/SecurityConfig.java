package com.udacity.jwdnd.course1.cloudstorage.config;

import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationService authenticationService;

    public SecurityConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF (for H2 Console only)
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable()) //Allow H2 Console
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/signup", "/h2-console/**", "/css/**", "/js/**").permitAll() //These can accessed by everyone
                        .requestMatchers("/chat").hasAnyAuthority("USER") //Only logged-in users can access /chat
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login").permitAll() // Show custom login page
                        .defaultSuccessUrl("/home", true)// Redirect to /chat after login
                        .failureUrl("/login?error=true") //Redirect on failure
                )
                .authenticationProvider(authenticationService)
                .build();
    }
}