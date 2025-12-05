package com.signservice.application.usecase

import PoiDocxToPdfConverter
import com.signservice.application.pdf.PdfSignatureService
import com.signservice.domain.SignatureRepository
import com.signservice.infra.files.FilesClient
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class GetSignedPdfUseCase(
    private val signatureRepository: SignatureRepository,
    private val filesClient: FilesClient,
    private val pdfSignatureService: PdfSignatureService,
    private val poiDocxToPdfConverter: PoiDocxToPdfConverter,
) {

    suspend fun execute(signatureId: UUID): SignedPdfDto {
        val signature = signatureRepository.findById(signatureId)
            ?: throw IllegalArgumentException("Signature $signatureId not found")

        val original = filesClient.downloadFile(signature.fileId)

        val pdf = poiDocxToPdfConverter.convert(original)

        val signedPdf = pdfSignatureService.applySignatureStampToPdf(pdf, signature)
        return SignedPdfDto(
            fileName = signature.fileName,
            pdf = signedPdf
        )
    }
}

