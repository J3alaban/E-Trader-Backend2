package com.debit.backend.dtos.requests

import lombok.Getter
import lombok.Setter

@Getter
@Setter
class UpdateSubCategoryRequest {

    private val id: Long? = null
    private val name: String? = null
    private val categoryId: Long? = null
}