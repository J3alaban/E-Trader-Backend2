package com.debit.backend.dtos.requests;

import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    public class CreateSubCategoryRequest {

        private String name;
        private Long categoryId;
    }

