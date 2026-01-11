package com.ecommerce.backend.repositories;

import com.ecommerce.backend.entities.WishlistProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistProductRepository extends JpaRepository<WishlistProduct, Long> {

}