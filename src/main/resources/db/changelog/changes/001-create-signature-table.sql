--liquibase formatted sql

--changeset signature-service:001-create-signature-table
CREATE TABLE signature (
    id UUID PRIMARY KEY,
    signer_name TEXT NOT NULL,
    signer_position TEXT NOT NULL,
    signer_organization TEXT NOT NULL,
    certificate_serial_number TEXT NOT NULL,
    certificate_valid_from TIMESTAMP WITH TIME ZONE NOT NULL,
    certificate_valid_to TIMESTAMP WITH TIME ZONE NOT NULL,
    is_certificate_valid_at_signing_time BOOLEAN NOT NULL,
    signed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    signature_bytes BYTEA NOT NULL,
    signature_base64 TEXT,
    file_id TEXT NOT NULL,
    file_name TEXT NOT NULL,
    file_hash TEXT NOT NULL,
    file_size BIGINT,
    file_mime_type TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

