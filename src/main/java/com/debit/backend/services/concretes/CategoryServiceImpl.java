package com.debit.backend.services.concretes;

import com.debit.backend.dtos.requests.CategoryRequestDTO;
import com.debit.backend.dtos.responses.CategoryResponse;
import com.debit.backend.dtos.responses.ProductResponseDTO;
import com.debit.backend.entities.Category;
import com.debit.backend.entities.Product;
import com.debit.backend.entities.SubCategories;
import com.debit.backend.mappers.CategoryMapper;
import com.debit.backend.mappers.ProductMapper;
import com.debit.backend.repositories.CategoryRepository;
import com.debit.backend.services.abstracts.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse create(CategoryRequestDTO requestDTO) {

        if (categoryRepository.findBySlug(requestDTO.getSlug()).isPresent()) {
            throw new RuntimeException("Category with this slug already exists");
        }

        Category category = categoryMapper.toEntity(requestDTO);

        return categoryMapper.toResponse(
                categoryRepository.save(category)
        );
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequestDTO requestDTO) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(requestDTO.getName());
        category.setSlug(requestDTO.getSlug());

        return categoryMapper.toResponse(
                categoryRepository.save(category)
        );
    }

    @Override
    public void delete(Long id) {

        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }

        categoryRepository.deleteById(id);
    }


}