package com.one.bootkafka.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${springdoc.info.title}")
    private String API_TITLE;

    @Value("${springdoc.info.description}")
    private String API_DESCRIPTION;

    @Value("${springdoc.info.version}")
    private String API_VERSION;

    /**
     * Swagger OpenAPI 기본 설정
     */
    @Bean
    public OpenAPI openAPI() {

//        SecurityScheme securityScheme = new SecurityScheme()
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat("JWT")
//                .in(SecurityScheme.In.HEADER)
//                .name("Authorization");
//
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());

//                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
//                .info(apiInfo())
//                .security(Collections.singletonList(securityRequirement));
    }

    /**
     * Swagger 기본 정보
     * @return
     */
    private Info apiInfo() {
        return new Info()
                .title(API_TITLE) // API의 제목
                .version(API_VERSION) // API의 버전
                .description(API_DESCRIPTION); // API에 대한 설명
    }
}