package com.ecommerce.backend.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DimensionsRequest {
    private Double width;
    private Double height;
    private Double depth;
}