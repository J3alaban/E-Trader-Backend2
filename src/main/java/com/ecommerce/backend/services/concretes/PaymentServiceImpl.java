package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.dtos.requests.PaymentRequest;
import com.ecommerce.backend.dtos.responses.PaymentResponse;
import com.ecommerce.backend.entities.*;
import com.ecommerce.backend.mappers.PaymentMapper;
import com.ecommerce.backend.repositories.AddressRepository;
import com.ecommerce.backend.repositories.OrderRepository;
import com.ecommerce.backend.repositories.PaymentRepository;
import com.ecommerce.backend.services.abstracts.PaymentProviderService;
import com.ecommerce.backend.services.abstracts.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentProviderService paymentProviderService;


    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Address address = null;
        if (request.getAddressId() != null) {
            address = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new RuntimeException("Address not found"));
            order.setAddress(address);
            orderRepository.save(order);
        }

        Payment payment = paymentMapper.toEntity(request, order, address);
        payment.setAmount(BigDecimal.valueOf(order.getTotalPrice()));
        payment.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(payment);

        payment = paymentProviderService.processPayment(payment);
        paymentRepository.save(payment);

        return paymentMapper.toResponse(payment);
    }




    // IYZICO FORM BAŞLATMA
    @Override
    @Transactional
    public PaymentResponse startIyzico(PaymentRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Address address = null;
        if (request.getAddressId() != null) {
            address = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new RuntimeException("Address not found"));
            order.setAddress(address);
            orderRepository.save(order);
        } else if (order.getAddress() != null) {
            address = order.getAddress();
        } else {

            address = new Address();

            address.setStreet("Adres yok");
            address.setCity("Şehir");
            address.setState("İl"); // örnek değer
            address.setZipCode("00000");
            address.setCountry("Türkiye");




            addressRepository.save(address);

            order.setAddress(address);
            orderRepository.save(order);
        }

        User user = order.getUser();
        if (user == null) {
            // Misafir kullanıcı için geçici user oluştur
            user = new User();
            user.setFirstName("Guest");
            user.setLastName("User");
            user.setEmail("guest-" + UUID.randomUUID() + "@guest.com");
            // Database'e kaydetmeye gerek yok
        }

        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new RuntimeException("Order has no products");
        }

        Payment payment = paymentMapper.toEntity(request, order, address);
        payment.setAmount(BigDecimal.valueOf(order.getTotalPrice()));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setAddress(address); // null hatasını önlemek için

        // Payment kaydı mutlaka olmalı ki ID dolsun
        paymentRepository.save(payment);

        String checkoutFormContent =
                paymentProviderService.initializeIyzicoForm(payment);

        payment.setCheckoutFormContent(checkoutFormContent);
        paymentRepository.save(payment); // tekrar kaydet

        PaymentResponse response = paymentMapper.toResponse(payment);
        response.setCheckoutFormContent(checkoutFormContent);
        return response;
    }



    // IYZICO CALLBACK
    @Override
    @Transactional
    public void completeIyzicoPayment(String token) {

        Payment payment = paymentRepository.findByTransactionId(token)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.PAID);
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);
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