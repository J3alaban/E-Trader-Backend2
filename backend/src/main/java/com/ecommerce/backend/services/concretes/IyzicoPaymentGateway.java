package com.ecommerce.backend.services.concretes;


import com.ecommerce.backend.entities.Payment;
import com.ecommerce.backend.entities.PaymentProvider;
import com.ecommerce.backend.entities.PaymentStatus;
import com.ecommerce.backend.services.abstracts.PaymentGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class IyzicoPaymentGateway implements PaymentGateway {

    @Override
    public boolean supports(Payment payment) {
        return payment.getProvider() == PaymentProvider.IYZICO;
    }

    @Override
    public Payment pay(Payment payment) {
        payment.setStatus(PaymentStatus.PAID);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setPaymentDate(LocalDateTime.now());
        return payment;
    }
}