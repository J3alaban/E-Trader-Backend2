package com.debit.backend.mappers;

import com.debit.backend.dtos.requests.DimensionsRequest;
import com.debit.backend.dtos.responses.DimensionsResponse;
import com.debit.backend.entities.Dimensions;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
@Mapper(componentModel = "spring")
public interface DimensionsMapper {

    Dimensions toEntity(DimensionsRequest request);

    DimensionsResponse toResponse(Dimensions dimensions);
}