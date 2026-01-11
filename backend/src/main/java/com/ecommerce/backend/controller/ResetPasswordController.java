package com.ecommerce.backend.controller;

import com.ecommerce.backend.dtos.requests.ForgotPasswordRequest;
import com.ecommerce.backend.dtos.requests.ResetPasswordRequest;
import com.ecommerce.backend.dtos.responses.ForgotPasswordResponse;
import com.ecommerce.backend.dtos.responses.ResetPasswordResponse;
import com.ecommerce.backend.services.abstracts.ResetPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @PostMapping("/forgot-password")   //http://localhost:8080/api/v1/auth/forgot-password
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        return ResponseEntity.ok(resetPasswordService.forgotPassword(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        return ResponseEntity.ok(resetPasswordService.resetPassword(request));
    }


}