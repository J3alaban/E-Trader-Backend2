package com.ecommerce.backend.entities;



import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "password_reset_tokens")
public class ResetPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static ResetPasswordToken create(User user, int expireMinutes) {
        return ResetPasswordToken.builder()
                .user(user)
                .token(RandomStringUtils.randomAlphanumeric(64))
                .expiresAt(LocalDateTime.now().plusMinutes(expireMinutes))
                .used(false)
                .build();
    }
}