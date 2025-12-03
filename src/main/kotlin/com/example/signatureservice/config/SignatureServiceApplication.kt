package com.example.signatureservice.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
@EnableConfigurationProperties(FilesServiceProperties::class)
class SignatureServiceApplication {
    @Bean
    fun filesWebClient(builder: WebClient.Builder, properties: FilesServiceProperties): WebClient {
        return builder.baseUrl(properties.baseUrl).build()
    }

    @Bean
    fun databaseClient(connectionFactory: ConnectionFactory): DatabaseClient {
        return DatabaseClient.builder()
            .connectionFactory(connectionFactory)
            .build()
    }
}

fun main(args: Array<String>) {
    runApplication<SignatureServiceApplication>(*args)
}

@Configuration
@ConfigurationProperties(prefix = "files-service")
@ConstructorBinding
data class FilesServiceProperties(
    val baseUrl: String
)

