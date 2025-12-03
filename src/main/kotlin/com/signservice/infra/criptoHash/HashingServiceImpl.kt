package com.signservice.infra.criptoHash

import com.signservice.application.HashingService
import org.bouncycastle.crypto.Digest
import org.bouncycastle.crypto.digests.GOST3411Digest
import org.springframework.stereotype.Component

@Component
class HashingServiceImpl : HashingService {

    override suspend fun calculateGOST3411Hash(bytes: ByteArray): String {
        val digest: Digest = GOST3411Digest()
        digest.update(bytes, 0, bytes.size)
        val hash = ByteArray(digest.digestSize)
        digest.doFinal(hash, 0)
        return hash.joinToString("") { "%02x".format(it) }
    }
}