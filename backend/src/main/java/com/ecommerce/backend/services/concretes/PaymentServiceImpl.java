package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.dtos.requests.PaymentRequest;
import com.ecommerce.backend.dtos.responses.PaymentResponse;
import com.ecommerce.backend.entities.Address;
import com.ecommerce.backend.entities.Order;
import com.ecommerce.backend.entities.Payment;
import com.ecommerce.backend.mappers.PaymentMapper;
import com.ecommerce.backend.repositories.AddressRepository; // Yeni eklendi
import com.ecommerce.backend.repositories.OrderRepository;
import com.ecommerce.backend.repositories.PaymentRepository;
import com.ecommerce.backend.services.abstracts.PaymentProviderService;
import com.ecommerce.backend.services.abstracts.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository; // Adresi bulmak için şart
    private final PaymentMapper paymentMapper;
    private final PaymentProviderService paymentProviderService;

    @Override
    @Transactional // Veritabanı tutarlılığı için önemli
    public PaymentResponse createPayment(PaymentRequest request) {
        // 1. Validasyon: Provider kontrolü en başta yapılmalı
        if (request.getProvider() == null) {
            throw new RuntimeException("Payment provider is required");
        }

        // 2. Siparişi Getir
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + request.getOrderId()));

        // 3. Adresi Getir
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + request.getAddressId()));

        // 4. Siparişin adresini güncelle (Opsiyonel ama önerilir: Sipariş teslimat adresi burada kesinleşir)
        order.setAddress(address);
        orderRepository.save(order);

        // 5. Entity Oluştur (Yeni mapper yapısına uygun: request, order, address)
        Payment payment = paymentMapper.toEntity(request, order, address);

        // Ödemeyi PENDING (Bekliyor) olarak ilk kez kaydet (Transaction ID almadan önce)
        paymentRepository.save(payment);

        // 6. Gerçek Ödeme Sağlayıcısı İşlemi (Iyzico, Stripe vb.)
        // Bu metodun içinde transactionId ve status güncellenmeli
        payment = paymentProviderService.processPayment(payment);

        // 7. Nihai Kayıt
        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponse(savedPayment);
    }

    @Override
    public PaymentResponse getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return paymentMapper.toResponse(payment);
    }

    @Override
    public List<PaymentResponse> getByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }
}