package com.ecommerce.backend.mappers;


import com.ecommerce.backend.dtos.responses.ReviewResponse;
import com.ecommerce.backend.entities.Review;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewResponse toResponse(Review review);

    List<ReviewResponse> toResponseList(List<Review> reviews);
}