package com.ecommerce.backend.services.abstracts;

import com.ecommerce.backend.entities.Payment;

public interface PaymentGateway {

    boolean supports(Payment payment);

    Payment pay(Payment payment);
    String initializeForm(Payment payment);
}