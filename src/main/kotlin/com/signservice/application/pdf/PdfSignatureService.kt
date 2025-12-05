package com.signservice.application.pdf

import com.signservice.domain.Signature

interface PdfSignatureService {
    suspend fun applySignatureStampToPdf(original: ByteArray, signature: Signature): ByteArray
}

