package com.ecommerce.backend.services.abstracts;

import com.ecommerce.backend.dtos.responses.OrderItemResponseDTO;

import java.util.List;

public interface OrderItemService {
    OrderItemResponseDTO getOrderItemById(Long id);
    List<OrderItemResponseDTO> getAllOrderItems();
    void deleteOrderItem(Long id);
}