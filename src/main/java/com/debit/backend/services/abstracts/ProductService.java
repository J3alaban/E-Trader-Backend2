package com.debit.backend.services.abstracts;

import com.debit.backend.dtos.requests.ProductRequestDTO;
import com.debit.backend.dtos.responses.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductResponseDTO> getAllProducts(Pageable pageable);

    ProductResponseDTO getProductById(Long id);


    ProductResponseDTO createProduct(Long userId, ProductRequestDTO productRequestDTO);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);

    void deleteProduct(Long id);

    ProductResponseDTO updateStock(Long id, Integer stock);

    Page<ProductResponseDTO> getProductsByCategory(Long categoryId, Pageable pageable);

    Page<ProductResponseDTO> getProductsBySubCategory(Long subCategoryId, Pageable pageable);
}