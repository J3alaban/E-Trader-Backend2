package com.ecommerce.backend.services.abstracts;

import com.ecommerce.backend.dtos.requests.PaymentRequest;
import com.ecommerce.backend.dtos.responses.PaymentResponse;

import java.util.List;

public interface PaymentService {

    // Temel ödeme oluşturma (Genel kullanım için)
    PaymentResponse createPayment(PaymentRequest request);

    // ID bazlı ödeme sorgulama
    PaymentResponse getById(Long id);

    // Siparişe ait ödemeleri listeleme
    List<PaymentResponse> getByOrderId(Long orderId);

    /**
     * Iyzico ödeme formunu başlatan ve frontend'e script dönen metod.
     */
    PaymentResponse startIyzico(PaymentRequest request);

    /**
     * Iyzico'dan gelen callback (geri bildirim) sonrası ödemeyi
     * doğrulayan ve siparişi onaylayan metod.
     * * @param token Iyzico tarafından gönderilen eşsiz işlem anahtarı
     */
    void completeIyzicoPayment(String token);
}