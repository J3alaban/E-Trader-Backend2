package com.ecommerce.backend.repositories;

import com.ecommerce.backend.entities.Category;
import com.ecommerce.backend.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByTitle(String title);

    Page<Product> findByCategory(Category category, Pageable pageable);

}
