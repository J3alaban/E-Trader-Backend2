package com.debit.backend.services.abstracts;

import com.debit.backend.dtos.requests.ForgotPasswordRequest;
import com.debit.backend.dtos.requests.ResetPasswordRequest;
import com.debit.backend.dtos.responses.ForgotPasswordResponse;
import com.debit.backend.dtos.responses.ResetPasswordResponse;

public interface ResetPasswordService {

    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);

    ResetPasswordResponse resetPassword(ResetPasswordRequest request);
}