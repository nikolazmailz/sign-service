package com.signservice.domain

import com.signservice.domain.model.Signature
import java.util.UUID

interface SignatureRepository {
    suspend fun save(signature: Signature): Signature
    suspend fun findById(id: UUID): Signature?
}

