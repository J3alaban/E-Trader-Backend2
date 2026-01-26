package com.ecommerce.backend.dtos.responses;
import com.ecommerce.backend.entities.PaymentMethod;
import com.ecommerce.backend.entities.PaymentProvider;
import com.ecommerce.backend.entities.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private PaymentMethod method;
    private PaymentStatus status;
    private BigDecimal amount;
    private String currency;
    private String transactionId;
    private LocalDateTime paymentDate;
    private PaymentProvider provider;
    private String address;
    private String checkoutFormContent;
}