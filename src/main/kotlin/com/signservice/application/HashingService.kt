package com.signservice.application

interface HashingService {
    suspend fun calculateGOST3411Hash(bytes: ByteArray): String
}

