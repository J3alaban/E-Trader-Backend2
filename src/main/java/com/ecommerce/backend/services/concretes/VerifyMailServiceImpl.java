package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.entities.EmailVerificationToken;
import com.ecommerce.backend.entities.User;
import com.ecommerce.backend.repositories.EmailVerificationTokenRepository;
import com.ecommerce.backend.repositories.UserRepository;
import com.ecommerce.backend.services.abstracts.VerifyMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerifyMailServiceImpl implements VerifyMailService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public boolean verifyToken(String token) {
        Optional<EmailVerificationToken> verificationTokenOpt = tokenRepository.findByToken(token);

        if (verificationTokenOpt.isPresent()) {
            EmailVerificationToken verificationToken = verificationTokenOpt.get();

            // 1. Token zaten kullanılmış mı veya süresi dolmuş mu kontrolü
            if (verificationToken.isUsed() || verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                return false;
            }

            // 2. Kullanıcıyı bul ve aktif et
            User user = verificationToken.getUser();
            user.setEmailVerified(true); // User entity'nizde enabled alanı olduğunu varsayıyoruz
            userRepository.save(user);

            // 3. Token'ı kullanıldı olarak işaretle (veya delete yapabilirsin)
            verificationToken.setUsed(true);
            tokenRepository.save(verificationToken);

            return true;
        }

        return false;
    }
}