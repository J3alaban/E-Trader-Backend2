package com.ecommerce.backend.controller;

import com.ecommerce.backend.dtos.responses.ProductResponseDTO;
import com.ecommerce.backend.entities.Wishlist;
import com.ecommerce.backend.services.abstracts.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("/{userId}")
    public List<ProductResponseDTO> getWishlist(@PathVariable Long userId) {
        return wishlistService.getWishlist(userId);
    }

    @PostMapping("/{userId}/{productId}")
    public List<ProductResponseDTO> addProduct(@PathVariable Long userId,
                                               @PathVariable Long productId) {
        wishlistService.addProduct(userId, productId);
        return wishlistService.getWishlist(userId);
    }

    @DeleteMapping("/{userId}/{productId}")
    public List<ProductResponseDTO> removeProduct(@PathVariable Long userId,
                                                  @PathVariable Long productId) {
        wishlistService.removeProduct(userId, productId);
        return wishlistService.getWishlist(userId);
    }
}