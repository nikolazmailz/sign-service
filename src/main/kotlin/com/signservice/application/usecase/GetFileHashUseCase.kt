package com.signservice.application.usecase

import com.signservice.application.HashingService
import com.signservice.infra.files.FilesClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class GetFileHashUseCase(
    private val filesClient: FilesClient,
    @Qualifier("gostHashingService")
    private val hashingService: HashingService
) {

    suspend fun execute(fileId: UUID): FileHashDto {
//        require(fileId.isNotBlank()) { "fileId is required" }
        val bytes = filesClient.downloadFile(fileId)
        val hash = hashingService.calculateGostHash(bytes)
        return FileHashDto(fileId = fileId, hash = hash)
    }
}

