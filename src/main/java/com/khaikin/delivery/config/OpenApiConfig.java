package com.khaikin.delivery.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Order & Delivery API",
                version = "1.0",
                description = "API for managing orders and delivery tracking"
        ),
        security = @SecurityRequirement(name = "bearerAuth") // Đảm bảo áp dụng bảo mật mặc định cho tất cả endpoint
)
@SecurityScheme(
        name = "bearerAuth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                                    .addSecuritySchemes("bearerAuth",
                                                        new io.swagger.v3.oas.models.security.SecurityScheme()
                                                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")))
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerAuth"));
    }

    @Bean
    public GroupedOpenApi apiV1Group() {
        return GroupedOpenApi.builder()
                .group("v1") // tên group hiện trên Swagger UI
                .pathsToMatch("/api/v1/**") // chỉ quét các endpoint v1
                .build();
    }

    @Bean
    public GroupedOpenApi apiV2Group() {
        return GroupedOpenApi.builder()
                .group("v2") // tên group hiện trên Swagger UI
                .pathsToMatch("/api/v2/**") // chỉ quét các endpoint v2
                .build();
    }
}