package com.ecommerce.backend.mappers;

import com.ecommerce.backend.dtos.responses.ResetPasswordResponse;
import com.ecommerce.backend.entities.ResetPasswordToken;

import java.time.LocalDateTime;

public interface ResetPasswordMapper {

    static boolean isTokenValid(ResetPasswordToken token) {
        return !token.isUsed()
                && token.getExpiresAt().isAfter(LocalDateTime.now());
    }

    static ResetPasswordResponse toResponse(String message) {
        return new ResetPasswordResponse(message);
    }

}