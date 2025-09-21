package com.gabriel.pos_system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        // Reglas de Acceso Específicas
                        .requestMatchers("/users/**", "/business/**").hasRole("ADMIN") // Solo ADMINS
                        .requestMatchers("/pos/**", "/clients/**", "/categories/**", "/products/**")
                        .hasAnyRole("ADMIN", "USER") // ADMINS y USERS

                        // Rutas Públicas (esto se mantiene igual)
                        .requestMatchers("/login", "/forgot-password/**", "/verify-otp/**", "/reset-password/**",
                                "/business/logo", "/css/**", "/js/**",
                                "/images/**")
                        .permitAll()

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard", true) // Cambiado a /dashboard para una mejor UX
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());

        return http.build();
    }

}
