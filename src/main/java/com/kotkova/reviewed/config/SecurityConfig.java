package com.kotkova.reviewed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. PRAVIDLA PRO PŘÍSTUP K URL ADRESÁM
                .authorizeHttpRequests((requests) -> requests
                        // Tyto stránky a soubory jsou veřejné pro všechny
                        .requestMatchers("/", "/reviews/**", "/place/**", "/error", "/review/**", "/register", "/profil/**", "/places/**").permitAll()
                        .requestMatchers("/css/**", "/js/**","/*.css", "/*.js", "/*.png", "/*.jpg", "/image/**").permitAll()
                        .anyRequest().authenticated()
                )

                // 2. NASTAVENÍ PŘIHLAŠOVACÍ OBRAZOVKY
                .formLogin((form) -> form
                        .loginPage("/login") // TÍMTO ŘÍKÁME: "Použij naši stránku místo tvé ošklivé"
                        .defaultSuccessUrl("/", true) // Po přihlášení jdeme domů
                        .permitAll()
                )

                // 3. NASTAVENÍ ODHLÁŠENÍ
                .logout((logout) -> logout
                        .logoutSuccessUrl("/") // Po odhlášení jdeme na homepage
                        .permitAll()
                );

        return http.build();
    }

    // Tímto dočasně říkáme, že hesla v databázi NEJSOU zašifrovaná
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}