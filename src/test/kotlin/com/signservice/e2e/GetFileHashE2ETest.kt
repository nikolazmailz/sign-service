package com.signservice.e2e

import com.signservice.controllers.dto.HashResponse
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType

internal class GetFileHashE2ETest : BaseE2ETest() {

    @Test
    fun `should return file hash`() {
        val fileId = "test-file-id"
        val content = "test-content".encodeToByteArray()
        stubFile(fileId, content)
        val expectedHash = runBlocking { hashingService.calculateGostHash(content) }

        val response = webTestClient.get()
            .uri("/api/v1/hash?fileId=$fileId")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(HashResponse::class.java)
            .returnResult()
            .responseBody!!

        assertEquals(fileId, response.fileId)
        assertEquals(expectedHash, response.hash)
    }
}

