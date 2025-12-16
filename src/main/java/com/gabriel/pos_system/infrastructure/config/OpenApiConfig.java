package com.gabriel.pos_system.infrastructure.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "bearerAuth", // Nombre del esquema de seguridad (referenciado abajo)
        type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("POS System API")
                        .version("1.0.0")
                        .description("API REST para el sistema de Punto de Venta con Clean Architecture y DDD.")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("dev@pos-system.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                // Agregamos el requisito de seguridad globalmente a toda la API
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}