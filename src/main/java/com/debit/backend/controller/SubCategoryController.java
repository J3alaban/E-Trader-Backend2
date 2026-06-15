package com.debit.backend.controller;

import com.debit.backend.dtos.requests.CreateSubCategoryRequest;
import com.debit.backend.dtos.responses.SubCategoryResponse;
import com.debit.backend.services.abstracts.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subcategories")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @PostMapping
    public SubCategoryResponse create(
            @RequestBody CreateSubCategoryRequest request) {

        return subCategoryService.create(request);
    }

    @GetMapping
    public List<SubCategoryResponse> getAll() {

        return subCategoryService.getAll();
    }

    @GetMapping("/{id}")
    public SubCategoryResponse getById(
            @PathVariable Long id) {

        return subCategoryService.getById(id);
    }

    @PutMapping("/{id}")
    public SubCategoryResponse update(
            @PathVariable Long id,
            @RequestBody CreateSubCategoryRequest request) {

        return subCategoryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        subCategoryService.delete(id);
    }
}