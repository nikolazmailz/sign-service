package com.example.signatureservice.application.usecase

import com.example.signatureservice.application.exception.SignatureNotFoundException
import com.example.signatureservice.domain.SignatureId
import com.example.signatureservice.domain.SignatureRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetSignatureFileUseCase(
    private val signatureRepository: SignatureRepository
) {
    suspend fun execute(signatureId: String): SignatureFileOutput {
        val signature = signatureRepository.findById(toSignatureId(signatureId))
            ?: throw SignatureNotFoundException(signatureId)

        return SignatureFileOutput(
            content = signature.signatureBytes,
            fileName = "${signature.fileName}.sig"
        )
    }

    private fun toSignatureId(signatureId: String): SignatureId =
        SignatureId.fromUuid(UUID.fromString(signatureId))
}

data class SignatureFileOutput(
    val content: ByteArray,
    val fileName: String
)

