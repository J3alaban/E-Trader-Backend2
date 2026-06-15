package com.debit.backend.controller;

import com.debit.backend.dtos.requests.ProductRequestDTO;
import com.debit.backend.dtos.responses.ProductResponseDTO;
import com.debit.backend.services.abstracts.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // GET ALL (pagination)
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }


    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // CREATE
    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponseDTO> createProduct(
            @PathVariable Long userId,
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {

        return ResponseEntity.ok(
                productService.createProduct(userId, productRequestDTO)
        );
    }
    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO productRequestDTO) {

        return ResponseEntity.ok(
                productService.updateProduct(id, productRequestDTO)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // STOCK UPDATE
    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductResponseDTO> updateStock(
            @PathVariable Long id,
            @RequestParam Integer stock) {

        return ResponseEntity.ok(
                productService.updateStock(id, stock)
        );
    }

    // 🔥 NEW: SUBCATEGORY FILTER (correct architecture)
    @GetMapping("/sub-category/{subCategoryId}")
    public ResponseEntity<Page<ProductResponseDTO>> getBySubCategory(
            @PathVariable Long subCategoryId,
            Pageable pageable) {

        return ResponseEntity.ok(
                productService.getProductsBySubCategory(subCategoryId, pageable)
        );
    }
}