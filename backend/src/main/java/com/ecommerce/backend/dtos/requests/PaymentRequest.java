package com.ecommerce.backend.dtos.requests;

import com.ecommerce.backend.entities.PaymentMethod;
import com.ecommerce.backend.entities.PaymentProvider;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private Long orderId;
    private PaymentMethod method;
    private BigDecimal amount;
    private String currency;
    private PaymentProvider provider;

    private Long addressId;

}