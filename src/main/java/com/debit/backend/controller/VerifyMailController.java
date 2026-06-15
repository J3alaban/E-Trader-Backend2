package com.debit.backend.controller;

import com.debit.backend.services.abstracts.VerifyMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "https://demirayhidrolik.com")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class VerifyMailController {

    private final VerifyMailService verifyMailService;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        boolean isVerified = verifyMailService.verifyToken(token);

        if (isVerified) {
            // Frontend'in JSON bekleme ihtimaline karşı Map ile dönmek daha güvenlidir
            return ResponseEntity.ok(Map.of("message", "E-posta başarıyla doğrulandı!"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Geçersiz veya süresi dolmuş doğrulama linki!"));
        }
    }
}
