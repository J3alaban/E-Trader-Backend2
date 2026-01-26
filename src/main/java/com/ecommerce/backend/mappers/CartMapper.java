package com.ecommerce.backend.mappers;

import com.ecommerce.backend.dtos.responses.CartResponse;
import com.ecommerce.backend.entities.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface CartMapper {

    @Mapping(source = "id", target = "cartId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "items")
    CartResponse toResponse(Cart cart);
}