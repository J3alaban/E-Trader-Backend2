package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.dtos.responses.ProductResponseDTO;
import com.ecommerce.backend.entities.Product;
import com.ecommerce.backend.entities.Wishlist;
import com.ecommerce.backend.entities.WishlistProduct;
import com.ecommerce.backend.mappers.ProductMapper;
import com.ecommerce.backend.repositories.ProductRepository;
import com.ecommerce.backend.repositories.UserRepository;
import com.ecommerce.backend.repositories.WishlistRepository;
import com.ecommerce.backend.services.abstracts.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public List<ProductResponseDTO> getWishlist(Long userId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow();

        return wishlist.getProducts().stream()
                .map(WishlistProduct::getProduct)
                .map(productMapper::responseFromProduct)
                .toList();
    }

    @Override
    public Wishlist addProduct(Long userId, Long productId) {
        Wishlist wishlist = getOrCreateWishlist(userId);
        Product product = productRepository.findById(productId).orElseThrow();

        // Aynı ürün birden eklenebilir, her eklemede yeni WishlistProduct oluştur
        WishlistProduct wp = new WishlistProduct();
        wp.setProduct(product);
        wp.setWishlist(wishlist);

        wishlist.getProducts().add(wp);
        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist removeProduct(Long userId, Long productId) {
        Wishlist wishlist = getOrCreateWishlist(userId);

        // Sadece 1 adet silmek için findFirst
        wishlist.getProducts().stream()
                .filter(wp -> wp.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(wp -> wishlist.getProducts().remove(wp));

        return wishlistRepository.save(wishlist);
    }

    private Wishlist getOrCreateWishlist(Long userId) {
        return wishlistRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Wishlist w = new Wishlist();
                    w.setUser(userRepository.findById(userId).orElseThrow());
                    w.setProducts(new ArrayList<>());
                    return wishlistRepository.save(w);
                });
    }
}