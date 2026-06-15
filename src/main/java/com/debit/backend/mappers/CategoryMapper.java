package com.debit.backend.mappers;

import com.debit.backend.dtos.requests.CategoryRequestDTO;
import com.debit.backend.dtos.responses.CategoryResponse;
import com.debit.backend.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequestDTO request);

    @Mapping(target = "id", source = "id") // Açıkça ID eşleşmesini belirt
    @Mapping(target = "url", expression = "java(\"/category/\" + category.getSlug())")
    CategoryResponse toResponse(Category category);
}