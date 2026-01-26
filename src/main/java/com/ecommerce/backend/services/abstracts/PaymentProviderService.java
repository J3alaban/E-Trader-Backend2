package com.ecommerce.backend.services.abstracts;

import com.ecommerce.backend.entities.Payment;

public interface PaymentProviderService {
    Payment processPayment(Payment payment);
    String initializeIyzicoForm(Payment payment);
}