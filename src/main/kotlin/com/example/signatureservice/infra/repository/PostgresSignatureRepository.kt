package com.example.signatureservice.infra.repository

import com.example.signatureservice.domain.Signature
import com.example.signatureservice.domain.SignatureId
import com.example.signatureservice.domain.SignatureRepository
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import kotlinx.coroutines.reactor.awaitOneOrNull
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class PostgresSignatureRepository(private val databaseClient: DatabaseClient) : SignatureRepository {
    private companion object {
        const val insert = """
            INSERT INTO signature (
                id,
                signer_name,
                signer_position,
                signer_organization,
                certificate_serial_number,
                certificate_valid_from,
                certificate_valid_to,
                is_certificate_valid_at_signing_time,
                signed_at,
                signature_bytes,
                signature_base64,
                file_id,
                file_name,
                file_hash,
                file_size,
                file_mime_type
            ) VALUES (
                :id,
                :signerName,
                :signerPosition,
                :signerOrganization,
                :certificateSerialNumber,
                :certificateValidFrom,
                :certificateValidTo,
                :isCertificateValidAtSigningTime,
                :signedAt,
                :signatureBytes,
                :signatureBase64,
                :fileId,
                :fileName,
                :fileHash,
                :fileSize,
                :fileMimeType
            )
        """

        const val selectById = "SELECT * FROM signature WHERE id = :id"
        const val selectByFileId = "SELECT * FROM signature WHERE file_id = :fileId ORDER BY signed_at DESC LIMIT 1"
    }

    override suspend fun save(signature: Signature): Signature {
        databaseClient.sql(insert)
            .bind("id", signature.id.value)
            .bind("signerName", signature.signerName)
            .bind("signerPosition", signature.signerPosition)
            .bind("signerOrganization", signature.signerOrganization)
            .bind("certificateSerialNumber", signature.certificateSerialNumber)
            .bind("certificateValidFrom", signature.certificateValidFrom)
            .bind("certificateValidTo", signature.certificateValidTo)
            .bind("isCertificateValidAtSigningTime", signature.isCertificateValidAtSigningTime)
            .bind("signedAt", signature.signedAt)
            .bind("signatureBytes", signature.signatureBytes)
            .bind("signatureBase64", signature.signatureBase64 ?: signature.resolvedSignatureBase64)
            .bind("fileId", signature.fileId)
            .bind("fileName", signature.fileName)
            .bind("fileHash", signature.fileHash)
            .bind("fileSize", signature.fileSize)
            .bind("fileMimeType", signature.fileMimeType)
            .fetch()
            .rowsUpdated()
            .awaitSingle()
        return signature
    }

    override suspend fun findById(id: SignatureId): Signature? {
        return databaseClient.sql(selectById)
            .bind("id", id.value)
            .map(this::toSignature)
            .awaitOneOrNull()
    }

    override suspend fun findByFileId(fileId: String): Signature? {
        return databaseClient.sql(selectByFileId)
            .bind("fileId", fileId)
            .map(this::toSignature)
            .awaitOneOrNull()
    }

    private fun toSignature(row: Row, metadata: RowMetadata): Signature {
        val uuid = row.get("id", UUID::class.java)!!
        return Signature(
            id = SignatureId.fromUuid(uuid),
            signerName = row.get("signer_name", String::class.java)!!,
            signerPosition = row.get("signer_position", String::class.java)!!,
            signerOrganization = row.get("signer_organization", String::class.java)!!,
            certificateSerialNumber = row.get("certificate_serial_number", String::class.java)!!,
            certificateValidFrom = row.get("certificate_valid_from", java.time.OffsetDateTime::class.java)!!,
            certificateValidTo = row.get("certificate_valid_to", java.time.OffsetDateTime::class.java)!!,
            isCertificateValidAtSigningTime = row.get("is_certificate_valid_at_signing_time", Boolean::class.java)!!,
            signedAt = row.get("signed_at", java.time.OffsetDateTime::class.java)!!,
            signatureBytes = row.get("signature_bytes", ByteArray::class.java)!!,
            fileId = row.get("file_id", String::class.java)!!,
            fileName = row.get("file_name", String::class.java)!!,
            fileHash = row.get("file_hash", String::class.java)!!,
            fileSize = row.get("file_size", Long::class.java),
            fileMimeType = row.get("file_mime_type", String::class.java),
            signatureBase64 = row.get("signature_base64", String::class.java)
        )
    }
}
