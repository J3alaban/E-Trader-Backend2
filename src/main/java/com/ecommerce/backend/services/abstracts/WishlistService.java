package com.ecommerce.backend.services.abstracts;

import com.ecommerce.backend.dtos.responses.ProductResponseDTO;
import com.ecommerce.backend.entities.Wishlist;

import java.util.List;

public interface WishlistService {

    List<ProductResponseDTO> getWishlist(Long userId);

    Wishlist addProduct(Long userId, Long productId);

    Wishlist removeProduct(Long userId, Long productId);
}
