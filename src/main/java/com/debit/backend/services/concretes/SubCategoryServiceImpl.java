package com.debit.backend.services.concretes;

import com.debit.backend.dtos.requests.CreateSubCategoryRequest;
import com.debit.backend.dtos.responses.SubCategoryResponse;
import com.debit.backend.entities.Category;
import com.debit.backend.entities.SubCategories;
import com.debit.backend.mappers.SubCategoryMapper;
import com.debit.backend.repositories.CategoryRepository;
import com.debit.backend.repositories.SubCategoryRepository;
import com.debit.backend.services.abstracts.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryMapper subCategoryMapper;

    @Override
    public SubCategoryResponse create(CreateSubCategoryRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow();

        SubCategories subCategory =
                subCategoryMapper.toEntity(request);

        subCategory.setCategory(category);

        return subCategoryMapper.toResponse(
                subCategoryRepository.save(subCategory)
        );
    }

    @Override
    public List<SubCategoryResponse> getAll() {

        return subCategoryRepository.findAll()
                .stream()
                .map(subCategoryMapper::toResponse)
                .toList();
    }

    @Override
    public SubCategoryResponse getById(Long id) {

        return subCategoryMapper.toResponse(
                subCategoryRepository.findById(id)
                        .orElseThrow()
        );
    }

    @Override
    public SubCategoryResponse update(Long id, CreateSubCategoryRequest request) {

        SubCategories existing = subCategoryRepository.findById(id)
                .orElseThrow();

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow();

        existing.setName(request.getName());
        existing.setCategory(category);

        return subCategoryMapper.toResponse(
                subCategoryRepository.save(existing)
        );
    }

    @Override
    public void delete(Long id) {

        subCategoryRepository.deleteById(id);
    }
}