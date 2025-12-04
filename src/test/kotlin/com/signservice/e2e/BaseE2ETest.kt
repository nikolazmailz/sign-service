package com.signservice.e2e

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestInstance
import org.springframework.test.context.junit.jupiter.Testcontainers
import org.springframework.test.web.reactive.server.WebTestClient
import com.signservice.application.HashingService
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.TestInstance.Lifecycle

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(Lifecycle.PER_CLASS)
abstract class BaseE2ETest {

    companion object {
        private const val WIREMOCK_PORT = 8089
        private const val WIREMOCK_BASE_URL = "http://localhost:$WIREMOCK_PORT"

        @Container
        @JvmStatic
        val postgres = PostgreSQLContainer<Nothing>("postgres:16-alpine")
            .apply {
                withDatabaseName("signature_service_test")
                withUsername("test_user")
                withPassword("test_password")
            }

        @DynamicPropertySource
        @JvmStatic
        fun dynamicProperties(registry: DynamicPropertyRegistry) {
            val r2dbcUrl = "r2dbc:postgresql://${postgres.host}:${postgres.firstMappedPort}/${postgres.databaseName}"
            registry.add("spring.r2dbc.url") { r2dbcUrl }
            registry.add("spring.r2dbc.username") { postgres.username }
            registry.add("spring.r2dbc.password") { postgres.password }
            registry.add("spring.liquibase.url") { postgres.jdbcUrl }
            registry.add("spring.liquibase.user") { postgres.username }
            registry.add("spring.liquibase.password") { postgres.password }
            registry.add("files.base-url") { WIREMOCK_BASE_URL }
        }
    }

    protected val wireMockServer = WireMockServer(wireMockConfig().port(WIREMOCK_PORT))

    @Autowired
    protected lateinit var webTestClient: WebTestClient

    @Autowired
    protected lateinit var databaseClient: DatabaseClient

    @Autowired
    protected lateinit var hashingService: HashingService

    @BeforeAll
    fun startWireMock() {
        wireMockServer.start()
    }

    @AfterAll
    fun stopWireMock() {
        wireMockServer.stop()
    }

    @BeforeEach
    fun resetWireMock() {
        wireMockServer.resetAll()
    }

    @BeforeEach
    fun cleanDatabase() {
        runBlocking {
            databaseClient
                .sql("TRUNCATE signature CASCADE")
                .then()
                .awaitFirstOrNull()
        }
    }

    protected fun stubFile(fileId: String, content: ByteArray, contentType: String = "application/octet-stream") {
        wireMockServer.stubFor(
            get(urlEqualTo("/files/$fileId"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", contentType)
                        .withBody(content)
                )
        )
    }
}

