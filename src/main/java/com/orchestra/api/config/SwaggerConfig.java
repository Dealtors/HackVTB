package com.orchestra.api.config;

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