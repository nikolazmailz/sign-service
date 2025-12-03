package com.example.signatureservice.application.usecase

import com.example.signatureservice.application.exception.SignatureNotFoundException
import com.example.signatureservice.domain.FilesClient
import com.example.signatureservice.domain.PdfSignatureService
import com.example.signatureservice.domain.SignatureId
import com.example.signatureservice.domain.SignatureRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetSignedPdfUseCase(
    private val signatureRepository: SignatureRepository,
    private val filesClient: FilesClient,
    private val pdfSignatureService: PdfSignatureService
) {
    suspend fun execute(signatureId: String): ByteArray {
        val signature = signatureRepository.findById(toSignatureId(signatureId))
            ?: throw SignatureNotFoundException(signatureId)

        val originalFileBytes = filesClient.downloadFile(signature.fileId)
        return pdfSignatureService.applySignatureStampToPdf(originalFileBytes, signature)
    }

    private fun toSignatureId(signatureId: String): SignatureId =
        SignatureId.fromUuid(UUID.fromString(signatureId))
}

