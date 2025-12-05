package com.signservice.application.pdf

interface DocxToPdfConverter {
    suspend fun convert(docx: ByteArray): ByteArray
}