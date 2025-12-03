package com.example.signatureservice.application.dto.response

import java.time.OffsetDateTime

data class CreateSignatureResponse(
    val id: String,
    val fileId: String,
    val fileName: String,
    val fileHash: String,
    val signedAt: OffsetDateTime
)

