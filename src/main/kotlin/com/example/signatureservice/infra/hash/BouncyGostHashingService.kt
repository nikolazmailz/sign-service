package com.example.signatureservice.infra.hash

import com.example.signatureservice.domain.HashingService
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.security.Security

@Component
class BouncyGostHashingService : HashingService {
    companion object {
        private const val GOST_ALGORITHM = "GOST3411"
    }

    init {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(BouncyCastleProvider())
        }
    }

    override suspend fun calculateGostHash(content: ByteArray): String {
        val messageDigest = MessageDigest.getInstance(GOST_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME)
        val hash = messageDigest.digest(content)
        return hash.joinToString("") { "%02x".format(it) }
    }
}

