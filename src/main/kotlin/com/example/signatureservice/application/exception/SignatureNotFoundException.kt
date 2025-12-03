package com.example.signatureservice.application.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class SignatureNotFoundException(signatureId: String) : ResponseStatusException(
    HttpStatus.NOT_FOUND,
    "Signature with id $signatureId was not found"
)

