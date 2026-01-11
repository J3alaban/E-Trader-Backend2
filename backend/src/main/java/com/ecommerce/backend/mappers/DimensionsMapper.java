package com.ecommerce.backend.mappers;

import com.ecommerce.backend.dtos.requests.DimensionsRequest;
import com.ecommerce.backend.dtos.responses.DimensionsResponse;
import com.ecommerce.backend.entities.Dimensions;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
@Mapper(componentModel = "spring")
public interface DimensionsMapper {

    Dimensions toEntity(DimensionsRequest request);

    DimensionsResponse toResponse(Dimensions dimensions);
}