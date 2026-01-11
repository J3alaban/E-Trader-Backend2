package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.entities.Payment;
import com.ecommerce.backend.entities.PaymentProvider;
import com.ecommerce.backend.entities.PaymentStatus;
import com.ecommerce.backend.services.abstracts.PaymentGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StripePaymentGateway implements PaymentGateway {

    @Override
    public boolean supports(Payment payment) {
        return payment.getProvider() == PaymentProvider.STRIPE;
    }

    @Override
    public Payment pay(Payment payment) {
        payment.setStatus(PaymentStatus.PAID);
        payment.setTransactionId("stripe_generated_id");
        payment.setPaymentDate(LocalDateTime.now());
        return payment;
    }
}