package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.dtos.responses.OrderItemResponseDTO;
import com.ecommerce.backend.entities.OrderItem;
import com.ecommerce.backend.mappers.OrderItemMapper;
import com.ecommerce.backend.repositories.OrderItemRepository;
import com.ecommerce.backend.services.abstracts.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderItemResponseDTO getOrderItemById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));
        return orderItemMapper.responseFromOrderItem(orderItem);
    }

    @Override
    public List<OrderItemResponseDTO> getAllOrderItems() {
        return orderItemRepository.findAll()
                .stream()
                .map(orderItemMapper::responseFromOrderItem)
                .toList();
    }

    @Override
    public void deleteOrderItem(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));
        orderItemRepository.delete(orderItem);
    }
}
