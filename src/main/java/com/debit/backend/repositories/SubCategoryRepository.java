package com.debit.backend.repositories;

import com.debit.backend.entities.SubCategories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategories, Long> {

    List<SubCategories> findByCategoryId(Long categoryId);

    boolean existsByIdAndCategoryId(Long subCategoryId, Long categoryId);
}