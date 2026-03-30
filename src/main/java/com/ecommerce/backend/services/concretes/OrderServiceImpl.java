package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.dtos.requests.OrderRequestDTO;
import com.ecommerce.backend.dtos.requests.OrderStatusRequestDTO;
import com.ecommerce.backend.dtos.responses.OrderResponseDTO;
import com.ecommerce.backend.entities.Order;
import com.ecommerce.backend.entities.OrderItem;
import com.ecommerce.backend.entities.Product;
import com.ecommerce.backend.entities.User;
import com.ecommerce.backend.mappers.OrderMapper;
import com.ecommerce.backend.repositories.OrderRepository;
import com.ecommerce.backend.repositories.ProductRepository;
import com.ecommerce.backend.repositories.UserRepository;
import com.ecommerce.backend.services.abstracts.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {

        User user = null;
        if (dto.getUserId() != null && dto.getUserId() != 0) {
            user = userRepository.findById(dto.getUserId())
                    .orElse(null); // orElseThrow yerine orElse(null)
        }
        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(dto.getTotalPrice());
        order.setOrderDate(dto.getOrderDate() != null ? dto.getOrderDate() : LocalDateTime.now());

        dto.getItems().forEach(itemDto -> {

            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(product.getPrice());

            order.getItems().add(item);
        });

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponseDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getItems() != null) {
            order.getItems().size(); // lazy load
        }

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
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(Order.OrderStatus.valueOf(dto.getStatus()));

        return orderMapper.toResponseDTO(orderRepository.save(order));
    }

    @Override
    public void cancelOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

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

