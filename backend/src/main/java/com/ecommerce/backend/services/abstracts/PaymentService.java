package com.ecommerce.backend.services.abstracts;
import com.ecommerce.backend.dtos.requests.PaymentRequest;
import com.ecommerce.backend.dtos.responses.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse getById(Long id);

    List<PaymentResponse> getByOrderId(Long orderId);
}