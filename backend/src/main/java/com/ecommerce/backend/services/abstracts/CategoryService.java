package com.ecommerce.backend.services.abstracts;

import com.ecommerce.backend.dtos.requests.CategoryRequestDTO;
import com.ecommerce.backend.dtos.responses.CategoryResponse;
import com.ecommerce.backend.dtos.responses.ProductResponseDTO;

import java.util.List;

public interface CategoryService {

    // Tüm kategorileri listeler
    List<CategoryResponse> getAll();

    // Yeni kategori oluşturur
    CategoryResponse create(CategoryRequestDTO requestDTO);

    // Mevcut kategoriyi günceller
    CategoryResponse update(Long id, CategoryRequestDTO requestDTO);

    // Kategoriyi ID ile siler
    void delete(Long id);

    // Kategoriye ait ürünleri getirir
    List<ProductResponseDTO> getProductsByCategorySlug(String slug);
}