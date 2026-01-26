package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.entities.Payment;
import com.ecommerce.backend.services.abstracts.PaymentGateway;
import com.ecommerce.backend.services.abstracts.PaymentProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentProviderServiceImpl implements PaymentProviderService {

    private final List<PaymentGateway> gateways;

    @Override
    public Payment processPayment(Payment payment) {
        return gateways.stream()
                .filter(gateway -> gateway.supports(payment))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unsupported payment provider"))
                .pay(payment);
    }

    @Override
    public String initializeIyzicoForm(Payment payment) {
        return gateways.stream()
                .filter(gateway -> gateway.supports(payment))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Gateway not found"))
                .initializeForm(payment); // Gateway'deki metodu çağırır
    }




}