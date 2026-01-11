package com.ecommerce.backend.controller;

import com.ecommerce.backend.dtos.requests.CartItemRequest;
import com.ecommerce.backend.dtos.responses.CartResponse;
import com.ecommerce.backend.services.abstracts.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Kullanıcının sepetini getir
    @GetMapping("/{userId}")
    public CartResponse getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    // Sepete ürün ekle
    @PostMapping("/{userId}/items")
    public CartResponse addItem(
            @PathVariable Long userId,
            @RequestBody CartItemRequest request
    ) {
        return cartService.addItemToCart(userId, request);
    }

    // Sepetten ürün sil
    @DeleteMapping("/{userId}/items/{productId}")
    public CartResponse removeItem(
            @PathVariable Long userId,
            @PathVariable Long productId
    ) {
        return cartService.removeItemFromCart(userId, productId);
    }

    // Sepeti temizle
    @DeleteMapping("/{userId}")
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }


    /* URLs
GET /api/v1/carts/{userId}

POST /api/v1/carts/{userId}/items

DELETE /api/v1/carts/{userId}/items/{productId}

DELETE /api/v1/carts/{userId}
     */
}