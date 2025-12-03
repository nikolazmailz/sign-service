package com.example.signatureservice.controllers

import com.example.signatureservice.application.dto.response.FileHashResponse
import com.example.signatureservice.application.usecase.GetFileHashUseCase
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/hash")
@Validated
class HashController(private val getFileHashUseCase: GetFileHashUseCase) {
    @GetMapping
    suspend fun calculateHash(@RequestParam @NotBlank fileId: String): FileHashResponse =
        getFileHashUseCase.execute(fileId)
}

