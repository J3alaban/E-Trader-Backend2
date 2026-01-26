package com.ecommerce.backend.controller;

import com.ecommerce.backend.dtos.requests.ProductRequestDTO;
import com.ecommerce.backend.dtos.responses.ProductResponseDTO;
import com.ecommerce.backend.services.abstracts.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 //@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;


     @GetMapping
     public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
             @RequestParam(required = false) String category,
             Pageable pageable
     ) {
         Page<ProductResponseDTO> response;

         if (category != null && !category.isBlank()) {
             response = productService.getProductsByCategory(category, pageable);
         } else {
             response = productService.getAllProducts(pageable);
         }

         return ResponseEntity.ok(response);
     }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct( @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO response = productService.createProduct(productRequestDTO);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO response = productService.updateProduct(id, productRequestDTO);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductResponseDTO> updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        ProductResponseDTO response = productService.updateStock(id, stock);
        return ResponseEntity.ok(response);
    }


}
