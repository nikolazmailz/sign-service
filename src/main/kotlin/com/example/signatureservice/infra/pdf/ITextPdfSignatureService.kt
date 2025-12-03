package com.example.signatureservice.infra.pdf

import com.example.signatureservice.domain.PdfSignatureService
import com.example.signatureservice.domain.Signature
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.font.constants.StandardFonts
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.Canvas
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.TextAlignment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.format.DateTimeFormatter

@Component
class ITextPdfSignatureService : PdfSignatureService {
    override suspend fun applySignatureStampToPdf(originalFile: ByteArray, signature: Signature): ByteArray =
        withContext(Dispatchers.Default) {
            ByteArrayOutputStream().use { outputStream ->
                PdfDocument(PdfReader(ByteArrayInputStream(originalFile)), PdfWriter(outputStream)).use { pdf ->
                    val page = pdf.lastPage
                    val stampWidth = 320f
                    val stampHeight = 140f
                    val marginX = 40f
                    val marginY = 40f
                    val rectangle = Rectangle(marginX, marginY, stampWidth, stampHeight)
                    val canvas = Canvas(PdfCanvas(page), pdf, rectangle)
                    canvas.setBackgroundColor(DeviceRgb(230, 230, 250))

                    val font = PdfFontFactory.createFont(StandardFonts.HELVETICA)
                    val text = buildStamp(signature)

                    val paragraph = Paragraph(text)
                        .setFont(font)
                        .setFontSize(10f)
                        .setTextAlignment(TextAlignment.LEFT)

                    canvas.add(paragraph)
                    canvas.close()
                }
                outputStream.toByteArray()
            }
        }

    private fun buildStamp(signature: Signature): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss O")
        return listOf(
            "Signer: ${signature.signerName} (${signature.signerPosition})",
            "Organization: ${signature.signerOrganization}",
            "Certificate: ${signature.certificateSerialNumber}",
            "Validity: ${formatter.format(signature.certificateValidFrom)} - ${formatter.format(signature.certificateValidTo)}",
            "Certificate valid at signing: ${signature.isCertificateValidAtSigningTime}",
            "Signed at: ${formatter.format(signature.signedAt)}",
            "File hash: ${signature.fileHash}"
        ).joinToString("\n")
    }
}

