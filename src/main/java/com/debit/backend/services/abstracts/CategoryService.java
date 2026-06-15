package com.debit.backend.services.abstracts;

import com.debit.backend.dtos.requests.CategoryRequestDTO;
import com.debit.backend.dtos.responses.CategoryResponse;

import java.util.List;

public interface CategoryService {

    // Tüm kategorileri getir
    List<CategoryResponse> getAll();

    // Yeni kategori oluştur
    CategoryResponse create(CategoryRequestDTO requestDTO);

    // Kategori güncelle
    CategoryResponse update(Long id, CategoryRequestDTO requestDTO);

    // Kategori sil
    void delete(Long id);
}