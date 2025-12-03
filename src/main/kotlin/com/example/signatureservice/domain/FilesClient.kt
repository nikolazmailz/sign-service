package com.example.signatureservice.domain

interface FilesClient {
    suspend fun downloadFile(fileId: String): ByteArray
}

