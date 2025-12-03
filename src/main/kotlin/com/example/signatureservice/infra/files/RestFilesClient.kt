package com.example.signatureservice.infra.files

import com.example.signatureservice.domain.FilesClient
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class RestFilesClient(private val filesWebClient: WebClient) : FilesClient {
    override suspend fun downloadFile(fileId: String): ByteArray {
        return filesWebClient.get()
            .uri("/files/{fileId}/content", fileId)
            .retrieve()
            .bodyToMono<ByteArray>()
            .awaitSingle()
    }
}

