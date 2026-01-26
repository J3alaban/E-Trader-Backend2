package com.ecommerce.backend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Double price;

    @NotNull
    private Integer stock;

    //  @NotBlank
    private String categorySlug;

    private String brand;
    private String sku;
    private String size ;

    private Double discountPercentage;
    private Integer weight;

    private DimensionsRequest dimensions;

    private List<String> images;

}
