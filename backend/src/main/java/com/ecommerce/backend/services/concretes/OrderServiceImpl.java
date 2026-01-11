package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.dtos.requests.OrderRequestDTO;
import com.ecommerce.backend.dtos.requests.OrderStatusRequestDTO;
import com.ecommerce.backend.dtos.responses.OrderResponseDTO;
import com.ecommerce.backend.entities.Order;
import com.ecommerce.backend.mappers.OrderMapper;
import com.ecommerce.backend.repositories.OrderRepository;
import com.ecommerce.backend.services.abstracts.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = orderMapper.orderFromRequest(orderRequestDTO);
        // Eğer orderDate null ise otomatik atama
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }
        Order savedOrder = orderRepository.save(order);
        return orderMapper.responseFromOrder(savedOrder);
    }

 /*   @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = orderMapper.orderFromRequest(orderRequestDTO);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.responseFromOrder(savedOrder);
    }  */


    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.responseFromOrder(order);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(orderMapper::responseFromOrder)
                .toList();
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long id, OrderStatusRequestDTO orderStatusRequestDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Order.OrderStatus status = Order.OrderStatus.valueOf(orderStatusRequestDTO.getStatus());
        order.setOrderStatus(status); // Status bilgisi güncelleniyor
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.responseFromOrder(updatedOrder);
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}

