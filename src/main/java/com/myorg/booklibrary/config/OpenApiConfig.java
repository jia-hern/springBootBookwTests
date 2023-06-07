package com.myorg.booklibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI OpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Book Library API")
                        .description("An API that can create books, readers and records")
                        .version("v1.0"));
    }
}
