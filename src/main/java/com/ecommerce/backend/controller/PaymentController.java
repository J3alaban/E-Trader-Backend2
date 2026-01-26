package com.ecommerce.backend.controller;
import com.ecommerce.backend.dtos.requests.PaymentRequest;
import com.ecommerce.backend.dtos.responses.PaymentResponse;
import com.ecommerce.backend.services.abstracts.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.services.concretes.PaymentServiceImpl;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.createPayment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getByOrderId(orderId));
    }

    @PostMapping("/iyzico-start")
    public ResponseEntity<PaymentResponse> startIyzico(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.startIyzico(request));
    }

    @PostMapping("/callback")
    public ResponseEntity<Void> iyzicoCallback(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        paymentService.completeIyzicoPayment(token);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("https://demirayhidrolik.com/payment-success"))
                .build();
    }



}