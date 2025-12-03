# Signature Service

Kotlin/Spring Boot microservice that orchestrates file hashing, signature persistence, and PDF stamping around customer signatures, following Clean Architecture boundaries.

## Prerequisites

- Java 21+
- Docker / Docker Compose for PostgreSQL
- A reachable `files` service serving raw file bytes (currently it is assumed to expose `GET /files/{fileId}/content`)

## Running PostgreSQL locally

```bash
docker compose up -d postgres
```

Postgres will run on port `5432` with:

| Property | Value |
|---------|-------|
| Database | `signature_service` |
| Username | `signature_user` |
| Password | `password` |

Liquibase migrations are triggered automatically on application start via `spring.liquibase`.

## Application

```bash
./gradlew bootRun
```

The service runs on port `8080` and connects to `files-service.base-url`, configurable in `src/main/resources/application.yml`.

## API

### `GET /api/v1/hash?fileId={fileId}`

Calls the upstream files service, computes a GOST hash, and returns it.

```bash
curl -X GET "http://localhost:8080/api/v1/hash?fileId=file-123"
```

Response:

```json
{
  "fileId": "file-123",
  "hash": "9f6a..."
}
```

### `POST /api/v1/signatures`

Creates a signature record. The payload requires signer data, certificate metadata, and the base64 signature.

```bash
curl -X POST http://localhost:8080/api/v1/signatures \
  -H "Content-Type: application/json" \
  -d '{
    "fileId": "file-123",
    "fileName": "document.pdf",
    "signatureBase64": "BASE64_SIGNATURE",
    "signerName": "Иван Иванов",
    "signerPosition": "Советник",
    "signerOrganization": "АО \"Пример\"",
    "certificateSerialNumber": "1234567890",
    "certificateValidFrom": "2025-01-01T00:00:00Z",
    "certificateValidTo": "2026-01-01T00:00:00Z",
    "isCertificateValidAtSigningTime": true,
    "signedAt": "2025-12-03T12:00:00Z"
  }'
```

### `GET /api/v1/signatures/{id}/sig`

Returns the raw `.sig` bytes with `Content-Disposition: attachment`.

```bash
curl -X GET http://localhost:8080/api/v1/signatures/{id}/sig -o signature.sig
```

### `GET /api/v1/signatures/{id}/pdf`

Combines the original file with the signature stamp and returns a PDF.

```bash
curl -X GET http://localhost:8080/api/v1/signatures/{id}/pdf -o signed.pdf
```

## Notes

- `FilesClient` currently expects `/files/{fileId}/content`; adjust `FilesServiceProperties` if your upstream differs.
- Hashing uses Bouncy Castle's `GOST3411`; replace or tune it if CryptoPro compatibility requirements evolve.
 - PDF stamping is implemented with iText and stamps signer/certificate metadata on the first page.