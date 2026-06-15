package com.debit.backend.services.concretes;

import com.debit.backend.dtos.requests.ForgotPasswordRequest;
import com.debit.backend.dtos.requests.ResetPasswordRequest;
import com.debit.backend.dtos.responses.ForgotPasswordResponse;
import com.debit.backend.dtos.responses.ResetPasswordResponse;
import com.debit.backend.entities.ResetPasswordToken;
import com.debit.backend.entities.User;
import com.debit.backend.mappers.ResetPasswordMapper;
import com.debit.backend.repositories.ResetPasswordTokenRepository;
import com.debit.backend.repositories.UserRepository;
import com.debit.backend.services.MailService;
import com.debit.backend.services.abstracts.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final UserRepository userRepository;
    private final ResetPasswordTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;


    @Override
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ResetPasswordToken token = ResetPasswordToken.create(user, 15);
        tokenRepository.save(token);
        mailService.sendResetPasswordMail(user.getEmail(), token.getToken());
        // mail gönderimi burada
        return new ForgotPasswordResponse ("Password reset link sent");
    }

    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {

        ResetPasswordToken token = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if (!ResetPasswordMapper.isTokenValid(token)) {
            throw new RuntimeException("Token expired or already used");
        }


        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        token.setUsed(true);

        userRepository.save(user);
        tokenRepository.save(token);


        return ResetPasswordMapper.toResponse("Password updated successfully");
    }
}