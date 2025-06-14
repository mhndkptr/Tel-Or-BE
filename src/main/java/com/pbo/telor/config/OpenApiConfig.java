package com.pbo.telor.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariables;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.servers.ServerVariable;

import java.util.List;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    ServerVariable versionVariable = new ServerVariable()
        ._default("v1")
        .description("API version")
        ._enum(List.of("v1", "v2"));

    Server localServer = new Server()
        .url("http://localhost:4646")
        .description("Local Server")
        .variables(new ServerVariables().addServerVariable("version", versionVariable));

    Server prodServer = new Server()
        .url("https://api.telor.muhhendikaputra.my.id/api/{version}")
        .description("Production Server")
        .variables(new ServerVariables().addServerVariable("version", versionVariable));

    return new OpenAPI()
        .info(new Info()
            .title("REST API Docs")
            .version("1.0.0")
            .description("Tel-Or API Documentation"))
        .servers(List.of(localServer, prodServer));
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
            "/api/v1/ormawa/**")
        .build();
  }
}
