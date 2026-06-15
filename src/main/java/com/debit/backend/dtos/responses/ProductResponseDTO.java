package com.debit.backend.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductResponseDTO {

    private Long id;

    private String title;
    private String description;

    private Long subCategoryId;
    private String subCategoryName;

    private Long categoryId;
    private String categoryName;

    private Double price;
    private Double discountPercentage;
    private Double rating;
    private Integer stock;

    private String size;

    private String brand;
    private String sku;
    private Integer weight;

    private String availabilityStatus;

    private List<String> images;
    private String thumbnail;

    private Long userId;
    private String userEmail;
}