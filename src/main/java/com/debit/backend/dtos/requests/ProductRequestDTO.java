package com.debit.backend.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProductRequestDTO {

    private String title;
    private String description;

    private Double price;
    private Integer stock;

    private Long subCategoryId;
    private Long categoryId;

    private String brand;
    private String sku;

    private Double discountPercentage;
    private Integer weight;

    private String size;

    private DimensionsRequest dimensions;

    private List<String> images;


    private Long userId;  // ürünü kime veriyorsun

    private LocalDateTime assignedAt;   // zimmet tarihi

    private LocalDateTime returnedAt;   // iade (opsiyonel)
}