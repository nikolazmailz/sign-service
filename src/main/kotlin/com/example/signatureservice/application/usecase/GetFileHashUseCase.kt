package com.example.signatureservice.application.usecase

import com.example.signatureservice.application.dto.response.FileHashResponse
import com.example.signatureservice.domain.FilesClient
import com.example.signatureservice.domain.HashingService
import org.springframework.stereotype.Service

@Service
class GetFileHashUseCase(
    private val filesClient: FilesClient,
    private val hashingService: HashingService
) {
    suspend fun execute(fileId: String): FileHashResponse {
        val fileBytes = filesClient.downloadFile(fileId)
        val hash = hashingService.calculateGostHash(fileBytes)
        return FileHashResponse(fileId = fileId, hash = hash)
    }
}

