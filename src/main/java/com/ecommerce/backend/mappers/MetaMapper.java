package com.ecommerce.backend.mappers;

import com.ecommerce.backend.dtos.responses.MetaResponse;
import com.ecommerce.backend.entities.Meta;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface MetaMapper {

    MetaResponse toResponse(Meta meta);
}