package com.example.signatureservice.controllers

import com.example.signatureservice.application.dto.request.CreateSignatureRequest
import com.example.signatureservice.application.dto.response.CreateSignatureResponse
import com.example.signatureservice.application.usecase.CreateSignatureUseCase
import com.example.signatureservice.application.usecase.GetSignatureFileUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/signatures")
@Validated
class SignatureController(
    private val createSignatureUseCase: CreateSignatureUseCase,
    private val getSignatureFileUseCase: GetSignatureFileUseCase
) {
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun createSignature(@Valid @RequestBody request: CreateSignatureRequest): CreateSignatureResponse =
        createSignatureUseCase.execute(request)

    @GetMapping("/{id}/sig")
    suspend fun downloadSignature(@PathVariable id: String): ResponseEntity<ByteArray> {
        val signatureFile = getSignatureFileUseCase.execute(id)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"${signatureFile.fileName}\""
            )
            .body(signatureFile.content)
    }
}

