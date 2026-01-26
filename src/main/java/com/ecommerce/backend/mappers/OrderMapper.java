package com.ecommerce.backend.mappers;

import com.ecommerce.backend.dtos.responses.OrderItemResponseDTO;
import com.ecommerce.backend.dtos.responses.OrderResponseDTO;
import com.ecommerce.backend.entities.Order;
import com.ecommerce.backend.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "items", expression = "java(mapItems(order.getItems()))")
    OrderResponseDTO responseFromOrder(Order order);

    @Mapping(source = "orderStatus", target = "status")
    OrderResponseDTO toResponseDTO(Order order);

    default List<OrderItemResponseDTO> mapItems(List<OrderItem> items) {
        if (items == null) return List.of();
        return items.stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getProduct().getId(),
                        item.getProduct().getTitle(),
                        item.getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());
    }
}


