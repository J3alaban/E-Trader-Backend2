package com.debit.backend.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DimensionsResponse {
    private Double width;
    private Double height;
    private Double depth;
}