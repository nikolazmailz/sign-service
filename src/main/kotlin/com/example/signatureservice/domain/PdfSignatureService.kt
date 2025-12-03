package com.example.signatureservice.domain

interface PdfSignatureService {
    suspend fun applySignatureStampToPdf(originalFile: ByteArray, signature: Signature): ByteArray
}

