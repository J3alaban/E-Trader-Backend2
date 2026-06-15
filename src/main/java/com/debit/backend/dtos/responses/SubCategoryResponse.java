package com.debit.backend.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCategoryResponse {

    private Long id;
    private String name;

    private Long categoryId;
    private String categoryName;
}