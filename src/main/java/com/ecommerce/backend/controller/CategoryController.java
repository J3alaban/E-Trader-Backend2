package com.ecommerce.backend.controller;

import com.ecommerce.backend.dtos.requests.CategoryRequestDTO;
import com.ecommerce.backend.dtos.responses.CategoryResponse;
import com.ecommerce.backend.dtos.responses.ProductResponseDTO;
import com.ecommerce.backend.services.abstracts.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{slug}/products")
    public List<ProductResponseDTO> getProductsByCategorySlug(@PathVariable String slug) {
        return categoryService.getProductsByCategorySlug(slug);
    }

    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@RequestBody CategoryRequestDTO requestDTO) {
        return categoryService.create(requestDTO);
    }

    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDTO requestDTO) {
        return categoryService.update(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }
}


