package com.kimberly.customermanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer Management API")
                        .version("1.0.0")
                        .description("REST API for managing customers — built with Spring Boot, Gradle & PostgreSQL")
                        .contact(new Contact()
                                .name("Kimberly Githinji")
                                .email("kimberlywanjiku28@gmail.com")
                                .url("https://www.kimberlygithinji.site")));
    }
}
