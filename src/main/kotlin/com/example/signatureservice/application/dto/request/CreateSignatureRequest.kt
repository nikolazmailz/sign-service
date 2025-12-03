package com.example.signatureservice.application.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime

data class CreateSignatureRequest(
    @field:NotBlank
    val fileId: String,
    @field:NotBlank
    val fileName: String,
    val fileSize: Long? = null,
    val fileMimeType: String? = null,
    @field:NotBlank
    val signatureBase64: String,
    @field:NotBlank
    val signerName: String,
    @field:NotBlank
    val signerPosition: String,
    @field:NotBlank
    val signerOrganization: String,
    @field:NotBlank
    val certificateSerialNumber: String,
    @field:NotNull
    val certificateValidFrom: OffsetDateTime,
    @field:NotNull
    val certificateValidTo: OffsetDateTime,
    @field:NotNull
    val isCertificateValidAtSigningTime: Boolean,
    @field:NotNull
    val signedAt: OffsetDateTime,
    val fileHash: String? = null
)

