package com.ecommerce.backend.services.abstracts;

import com.ecommerce.backend.dtos.requests.CartItemRequest;
import com.ecommerce.backend.dtos.responses.CartResponse;

public interface CartService {

    CartResponse getCartByUserId(Long userId);

    CartResponse addItemToCart(Long userId, CartItemRequest request);

    CartResponse removeItemFromCart(Long userId, Long productId);

    void clearCart(Long userId);
}