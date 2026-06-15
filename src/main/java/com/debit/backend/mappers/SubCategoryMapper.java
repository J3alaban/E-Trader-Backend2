package com.debit.backend.mappers;

import com.debit.backend.dtos.requests.CreateSubCategoryRequest;
import com.debit.backend.dtos.responses.SubCategoryResponse;
import com.debit.backend.entities.SubCategories;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubCategoryMapper {

    // request -> entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true) // service içinde set ediyorsun
    @Mapping(target = "products", ignore = true) // ilişkiyi DB yönetir
    SubCategories toEntity(CreateSubCategoryRequest request);

    // entity -> response
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    SubCategoryResponse toResponse(SubCategories entity);
}