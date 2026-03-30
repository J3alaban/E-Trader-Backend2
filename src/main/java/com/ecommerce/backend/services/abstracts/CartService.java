package com.ecommerce.backend.services.abstracts;

import com.ecommerce.backend.dtos.requests.CartItemRequest;
import com.ecommerce.backend.dtos.responses.CartResponse;

public interface CartService {

    CartResponse getCartByUserId(String userId);

    CartResponse addItemToCart(String userId, CartItemRequest request);

    CartResponse removeItemFromCart(String userId, Long productId);

    void clearCart(String userId);
}