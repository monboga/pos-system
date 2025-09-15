package com.gabriel.pos_system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.gabriel.pos_system.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Inyectamos nuestro servicio de detalles de usuario personalizado
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- NUEVO BEAN: El Proveedor de Autenticación ---
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Creamos el proveedor de autenticación que trabaja contra la base de datos
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Le decimos cuál es nuestro servicio para buscar usuarios
        authProvider.setUserDetailsService(customUserDetailsService);
        // Le decimos cuál es nuestro codificador de contraseñas
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Le decimos a Spring Security que use nuestro proveedor de autenticación
                // personalizado
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/forgot-password", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/index", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());

        return http.build();
    }

}
