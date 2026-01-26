package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.dtos.requests.ForgotPasswordRequest;
import com.ecommerce.backend.dtos.requests.ResetPasswordRequest;
import com.ecommerce.backend.dtos.responses.ForgotPasswordResponse;
import com.ecommerce.backend.dtos.responses.ResetPasswordResponse;
import com.ecommerce.backend.entities.ResetPasswordToken;
import com.ecommerce.backend.entities.User;
import com.ecommerce.backend.mappers.ResetPasswordMapper;
import com.ecommerce.backend.repositories.ResetPasswordTokenRepository;
import com.ecommerce.backend.repositories.UserRepository;
import com.ecommerce.backend.services.MailService;
import com.ecommerce.backend.services.abstracts.ResetPasswordService;
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