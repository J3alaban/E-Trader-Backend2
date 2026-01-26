package com.ecommerce.backend.mappers;

import com.ecommerce.backend.dtos.responses.OrderItemResponseDTO;
import com.ecommerce.backend.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.title")
    @Mapping(target = "productPrice", source = "price")
    @Mapping(target = "quantity", source = "quantity")
    OrderItemResponseDTO responseFromOrderItem(OrderItem orderItem);
}
