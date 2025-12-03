package com.example.signatureservice.application.usecase

import com.example.signatureservice.application.dto.request.CreateSignatureRequest
import com.example.signatureservice.application.dto.response.CreateSignatureResponse
import com.example.signatureservice.domain.FilesClient
import com.example.signatureservice.domain.HashingService
import com.example.signatureservice.domain.Signature
import com.example.signatureservice.domain.SignatureId
import com.example.signatureservice.domain.SignatureRepository
import org.springframework.stereotype.Service
import java.util.Base64

@Service
class CreateSignatureUseCase(
    private val filesClient: FilesClient,
    private val hashingService: HashingService,
    private val signatureRepository: SignatureRepository
) {
    suspend fun execute(request: CreateSignatureRequest): CreateSignatureResponse {
        val fileBytes = filesClient.downloadFile(request.fileId)
        val fileHash = request.fileHash ?: hashingService.calculateGostHash(fileBytes)
        val signatureBytes = Base64.getDecoder().decode(request.signatureBase64)

        val signature = Signature(
            id = SignatureId.random(),
            signerName = request.signerName,
            signerPosition = request.signerPosition,
            signerOrganization = request.signerOrganization,
            certificateSerialNumber = request.certificateSerialNumber,
            certificateValidFrom = request.certificateValidFrom,
            certificateValidTo = request.certificateValidTo,
            isCertificateValidAtSigningTime = request.isCertificateValidAtSigningTime,
            signedAt = request.signedAt,
            signatureBytes = signatureBytes,
            fileId = request.fileId,
            fileName = request.fileName,
            fileHash = fileHash,
            fileSize = request.fileSize ?: fileBytes.size.toLong(),
            fileMimeType = request.fileMimeType,
            signatureBase64 = request.signatureBase64
        )

        val saved = signatureRepository.save(signature)
        return CreateSignatureResponse(
            id = saved.id.toString(),
            fileId = saved.fileId,
            fileName = saved.fileName,
            fileHash = saved.fileHash,
            signedAt = saved.signedAt
        )
    }
}

