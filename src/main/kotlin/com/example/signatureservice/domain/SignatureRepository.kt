package com.example.signatureservice.domain

interface SignatureRepository {
    suspend fun save(signature: Signature): Signature
    suspend fun findById(id: SignatureId): Signature?
    suspend fun findByFileId(fileId: String): Signature?
}

