package com.signservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun filesServiceWebClient(): WebClient =
        WebClient.builder()
            .baseUrl("http://files-service/")
            .build()
}

