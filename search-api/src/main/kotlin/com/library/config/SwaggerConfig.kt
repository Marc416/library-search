package com.library.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun librarySearchAPI(): OpenAPI {
        return OpenAPI()
            .components(Components())
            .info(
                Info().title("Library Search API").version("v0.0.1")
                    .license(License().name("Apache 2.0").url("http://springdoc.org"))
            )
    }
}
