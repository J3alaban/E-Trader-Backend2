package com.ecommerce.backend.services.abstracts;

import com.ecommerce.backend.dtos.requests.ProductRequestDTO;
import com.ecommerce.backend.dtos.responses.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<ProductResponseDTO> getAllProducts(Pageable pageable);

    ProductResponseDTO getProductById(Long id);

    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);

    void deleteProduct(Long id);


    ProductResponseDTO updateStock(Long id, Integer stock);
    Page<ProductResponseDTO> getProductsByCategory(String category, Pageable pageable);

}
