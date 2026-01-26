package com.ecommerce.backend.services.abstracts;

import com.ecommerce.backend.dtos.requests.ForgotPasswordRequest;
import com.ecommerce.backend.dtos.requests.ResetPasswordRequest;
import com.ecommerce.backend.dtos.responses.ForgotPasswordResponse;
import com.ecommerce.backend.dtos.responses.ResetPasswordResponse;

public interface ResetPasswordService {

    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);

    ResetPasswordResponse resetPassword(ResetPasswordRequest request);
}