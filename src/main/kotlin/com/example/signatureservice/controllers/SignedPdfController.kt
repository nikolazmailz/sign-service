package com.example.signatureservice.controllers

import com.example.signatureservice.application.usecase.GetSignedPdfUseCase
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/signatures")
class SignedPdfController(private val getSignedPdfUseCase: GetSignedPdfUseCase) {
    @GetMapping("/{id}/pdf")
    suspend fun downloadStampedPdf(@PathVariable id: String): ResponseEntity<ByteArray> {
        val pdfBytes = getSignedPdfUseCase.execute(id)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"${id}.pdf\""
            )
            .body(pdfBytes)
    }
}

