package com.example.userLogin.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private SecurityScheme createBearerAuthScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    private OpenApiCustomizer createOpenApiCustomizer(String title, String version) {
        return openApi -> {
            openApi.info(new Info().title(title).version(version));
            openApi.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));  // Bearer Auth 추가
            openApi.schemaRequirement("bearerAuth", createBearerAuthScheme());  // bearerAuth를 scheme으로 추가
        };
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/**")
                .displayName("All API")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info().title("모든 API").version("v0.4"));
                    openApi.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
                    openApi.schemaRequirement("bearerAuth", new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .bearerFormat("JWT")
                            .scheme("bearer"));
                })
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/user/**")
                .displayName("User's API")
                .addOpenApiCustomizer(createOpenApiCustomizer("유저 관련 API", "v0.4"))
                .build();
    }

    @Bean
    public GroupedOpenApi ScheduleApi() {
        return GroupedOpenApi.builder()
            .group("schedule")
            .pathsToMatch("/schedule/**")
            .displayName("schedule's API")
            .addOpenApiCustomizer(createOpenApiCustomizer("일정 API", "v0.4"))
            .build();
    }

    @Bean
    public GroupedOpenApi CommentApi() {
        return GroupedOpenApi.builder()
            .group("comment")
            .pathsToMatch("/comment/**")
            .displayName("comment's API")
            .addOpenApiCustomizer(createOpenApiCustomizer("댓글 API", "v0.4"))
            .build();
    }

    @Bean
    public GroupedOpenApi AttendanceApi() {
        return GroupedOpenApi.builder()
                .group("attendance")
                .pathsToMatch("/attendance/**")
                .displayName("attendance's API")
                .addOpenApiCustomizer(createOpenApiCustomizer("QR Code API", "v0.4"))
                .build();
    }

}
