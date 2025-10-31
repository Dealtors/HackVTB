package com.orchestra.api.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI orchestraOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orchestra API")
                        .description("Платформа для автоматического тестирования бизнес-процессов")
                        .version("v1.0.0"));
    }
}