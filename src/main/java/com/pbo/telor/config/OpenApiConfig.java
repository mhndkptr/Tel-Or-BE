package com.pbo.telor.config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import io.swagger.v3.oas.models.servers.ServerVariables;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
        @Value("${server.port}")
        private String serverPort;

        @Bean
        public OpenAPI customOpenAPI() {
                ServerVariable versionVariable = new ServerVariable()
                                ._default("v1")
                                .description("API version")
                                ._enum(List.of("v1", "v2"));

                Server localServer = new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local Server")
                                .variables(new ServerVariables().addServerVariable("version", versionVariable));

                Server prodServer = new Server()
                                .url("https://api.telor.muhhendikaputra.my.id")
                                .description("Production Server")
                                .variables(new ServerVariables().addServerVariable("version", versionVariable));

                SecurityScheme bearerAuth = new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                                .description("Masukkan token JWT Anda di sini. Contoh: eyJhbGciOi...");
                SecurityRequirement securityRequirement = new SecurityRequirement()
                                .addList("bearerAuth");

                return new OpenAPI()
                                .info(new Info()
                                                .title("REST API Docs")
                                                .version("1.0.0")
                                                .description("Tel-Or API Documentation"))
                                .servers(List.of(localServer, prodServer))
                                .components(new io.swagger.v3.oas.models.Components()
                                                .addSecuritySchemes("bearerAuth", bearerAuth))
                                .addSecurityItem(securityRequirement);
        }

        @Bean
        public GroupedOpenApi publicApi() {
                return GroupedOpenApi.builder()
                                .group("public")
                                .pathsToMatch(
                                                "/api/v1/auth/**",
                                                "/api/v1/users/**",
                                                "/api/v1/faqs/**",
                                                "/api/v1/events/**",
                                                "/api/v1/upload/**",
                                                "/api/v1/landing/**",
                                                "/api/v1/ormawa/**")
                                .build();
        }
}
