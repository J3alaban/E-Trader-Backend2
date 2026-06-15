package com.debit.backend.repositories;

import com.debit.backend.entities.Category;
import com.debit.backend.entities.Product;
import com.debit.backend.entities.SubCategories;
import com.debit.backend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findBySubCategory(SubCategories subCategory, Pageable pageable);

    Page<Product> findByUser(User user, Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable); // 🔥 EKLENDİ
}