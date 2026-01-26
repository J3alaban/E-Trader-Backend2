package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.dtos.requests.CategoryRequestDTO;
import com.ecommerce.backend.dtos.responses.CategoryResponse;
import com.ecommerce.backend.dtos.responses.ProductResponseDTO;
import com.ecommerce.backend.entities.Category;
import com.ecommerce.backend.mappers.CategoryMapper;
import com.ecommerce.backend.mappers.ProductMapper;
import com.ecommerce.backend.repositories.CategoryRepository;
import com.ecommerce.backend.repositories.ProductRepository;
import com.ecommerce.backend.services.abstracts.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse create(CategoryRequestDTO requestDTO) {
        if (categoryRepository.findBySlug(requestDTO.getSlug()).isPresent()) {
            throw new RuntimeException("Category with this slug already exists");
        }
        Category category = categoryMapper.toEntity(requestDTO);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(requestDTO.getName());
        category.setSlug(requestDTO.getSlug());

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    public List<ProductResponseDTO> getProductsByCategorySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return productRepository.findByCategory(category, Pageable.unpaged())
                .getContent().stream()
                .map(productMapper::responseFromProduct)
                .toList();
    }
}