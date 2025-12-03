package com.signservice.infra.files

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class FilesClient(
    @Qualifier("filesServiceWebClient")
    private val webClient: WebClient
) {

    suspend fun downloadFile(fileId: String): ByteArray =
        webClient.get()
            .uri("/files/{id}", fileId)
            .retrieve()
            .awaitBody()
}

