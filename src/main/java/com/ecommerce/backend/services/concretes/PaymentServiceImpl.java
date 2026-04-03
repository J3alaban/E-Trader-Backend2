package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.dtos.requests.PaymentRequest;
import com.ecommerce.backend.dtos.responses.PaymentResponse;
import com.ecommerce.backend.entities.*;
import com.ecommerce.backend.mappers.PaymentMapper;
import com.ecommerce.backend.repositories.AddressRepository;
import com.ecommerce.backend.repositories.OrderRepository;
import com.ecommerce.backend.repositories.PaymentRepository;
import com.ecommerce.backend.repositories.UserRepository;
import com.ecommerce.backend.services.abstracts.PaymentProviderService;
import com.ecommerce.backend.services.abstracts.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentProviderService paymentProviderService;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Address address = resolveAddress(request, order);

        Payment payment = paymentMapper.toEntity(request, order, address);
        payment.setAmount(BigDecimal.valueOf(order.getTotalPrice()));
        payment.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(payment);
        payment = paymentProviderService.processPayment(payment);
        paymentRepository.save(payment);

        return paymentMapper.toResponse(payment);
    }

    // =====================================================
    // IYZICO START
    // =====================================================
    @Override
    @Transactional
    public PaymentResponse startIyzico(PaymentRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new RuntimeException("Order has no products");
        }

        Address address = resolveAddress(request, order);

        Payment payment = paymentMapper.toEntity(request, order, address);
        payment.setAmount(BigDecimal.valueOf(order.getTotalPrice()));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setAddress(address);

        // NOT: guest bilgileri (name/email/phone) Payment entity'de yok.
        // IyzicoPaymentGateway bu bilgileri payment.getOrder().getGuestName() vb. ile okur.
        // Order entity'ye zaten eklendi — burada ekstra bir şey yapmaya gerek yok.

        paymentRepository.save(payment);

        String checkoutFormContent = paymentProviderService.initializeIyzicoForm(payment);

        payment.setCheckoutFormContent(checkoutFormContent);
        paymentRepository.save(payment);

        PaymentResponse response = paymentMapper.toResponse(payment);
        response.setCheckoutFormContent(checkoutFormContent);

        return response;
    }

    // =====================================================
    // IYZICO CALLBACK — Payment PAID + Order SHIPPED
    // =====================================================
    @Override
    @Transactional
    public void completeIyzicoPayment(String token) {

        Payment payment = paymentRepository.findByTransactionId(token)
                .orElseThrow(() -> new RuntimeException("Payment not found for token: " + token));

        // Payment güncelle
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        // Order güncelle
        Order order = payment.getOrder();
        if (order != null) {
            order.setOrderStatus(Order.OrderStatus.SHIPPED);
            orderRepository.save(order);
        }
    }

    // =====================================================
    // ADDRESS RESOLVER
    // =====================================================
    private Address resolveAddress(PaymentRequest request, Order order) {

        // 1. Kayıtlı kullanıcı — seçili adres ID
        if (request.getAddressId() != null && request.getAddressId() != 0) {
            return addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new RuntimeException("Address not found: " + request.getAddressId()));
        }

        // 2. Sipariş üzerinde zaten bir adres varsa kullan
        if (order.getAddress() != null) {
            return order.getAddress();
        }

        // 3. Guest — serbest metin adres
        if (request.getGuestAddress() != null && !request.getGuestAddress().isBlank()) {
            Address guestAddress = new Address();
            guestAddress.setStreet(request.getGuestAddress());
            guestAddress.setCity("Guest");
            guestAddress.setCountry("TR");
            guestAddress.setState("N/A");
            guestAddress.setZipCode("00000");
            return addressRepository.save(guestAddress);
        }

        throw new RuntimeException("Teslimat adresi gereklidir.");
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