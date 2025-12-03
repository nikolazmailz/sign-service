package com.example.signatureservice.domain

import java.time.OffsetDateTime
import java.util.Base64
import java.util.UUID

@JvmInline
value class SignatureId(val value: UUID) {
    override fun toString(): String = value.toString()

    companion object {
        fun random(): SignatureId = SignatureId(UUID.randomUUID())
        fun fromUuid(uuid: UUID): SignatureId = SignatureId(uuid)
    }
}

data class Signature(
    val id: SignatureId,
    val signerName: String,
    val signerPosition: String,
    val signerOrganization: String,
    val certificateSerialNumber: String,
    val certificateValidFrom: OffsetDateTime,
    val certificateValidTo: OffsetDateTime,
    val isCertificateValidAtSigningTime: Boolean,
    val signedAt: OffsetDateTime,
    val signatureBytes: ByteArray,
    val fileId: String,
    val fileName: String,
    val fileHash: String,
    val fileSize: Long?,
    val fileMimeType: String?,
    val signatureBase64: String? = null
) {
    val resolvedSignatureBase64: String
        get() = signatureBase64 ?: Base64.getEncoder().encodeToString(signatureBytes)
}

