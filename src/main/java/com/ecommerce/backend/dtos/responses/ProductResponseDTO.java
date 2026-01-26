package com.ecommerce.backend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

    private Long id;

    private String title;
    private String description;
    private String category;
    private Double price;
    private Double discountPercentage;
    private Double rating;
    private Integer stock;
    private String size  ;

    private String brand;
    private String sku;
    private Integer weight;

    private String availabilityStatus;

    private List<String> images;
    private String thumbnail;

    private DimensionsResponse dimensions;
    private MetaResponse meta;

    private List<ReviewResponse> reviews;
}