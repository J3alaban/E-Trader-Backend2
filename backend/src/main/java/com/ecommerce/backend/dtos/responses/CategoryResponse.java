package com.ecommerce.backend.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private String slug;
    private String name;
    private String url;

    private Long id;

}




