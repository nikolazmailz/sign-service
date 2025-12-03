package com.example.signatureservice.domain

interface HashingService {
    suspend fun calculateGostHash(content: ByteArray): String
}

