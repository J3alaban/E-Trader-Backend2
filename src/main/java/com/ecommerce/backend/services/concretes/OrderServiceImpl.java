package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.dtos.requests.OrderRequestDTO;
import com.ecommerce.backend.dtos.requests.OrderStatusRequestDTO;
import com.ecommerce.backend.dtos.responses.OrderResponseDTO;
import com.ecommerce.backend.entities.*;
import com.ecommerce.backend.mappers.OrderMapper;
import com.ecommerce.backend.repositories.*;
import com.ecommerce.backend.services.abstracts.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {

        // 1. USER
        //    - Kayıtlı kullanıcıysa DB'den al.
        //    - Guest ise user NULL kalır; DB'ye hiçbir guest kaydı yazılmaz.
        User user = null;
        if (dto.getUserId() != null && dto.getUserId() != 0) {
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));
        }

        // 2. ORDER
        Order order = new Order();
        order.setUser(user);           // guest → null
        order.setAddress(null);        // adres ödeme adımında belirlenir
        order.setTotalPrice(dto.getTotalPrice());
        order.setOrderDate(dto.getOrderDate() != null ? dto.getOrderDate() : LocalDateTime.now());
        order.setOrderStatus(Order.OrderStatus.PENDING);

        // 3. GUEST BİLGİLERİ
        //    Kayıtlı kullanıcıda bu alanlar zaten null gelir, sorun yok.
        order.setGuestName(dto.getGuestName());
        order.setGuestEmail(dto.getGuestEmail());
        order.setGuestPhone(dto.getGuestPhone());

        // 4. ORDER ITEMS
        order.setItems(new ArrayList<>());

        dto.getItems().forEach(itemDto -> {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDto.getProductId()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(product.getPrice());

            order.getItems().add(item);
        });

        // 5. SAVE
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        if (order.getItems() != null) order.getItems().size();
        return orderMapper.toResponseDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(orderMapper::toResponseDTO)
                .toList();
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long id, OrderStatusRequestDTO dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        order.setOrderStatus(Order.OrderStatus.valueOf(dto.getStatus()));
        return orderMapper.toResponseDTO(orderRepository.save(order));
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        order.setOrderStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponseDTO)
                .toList();
    }
}