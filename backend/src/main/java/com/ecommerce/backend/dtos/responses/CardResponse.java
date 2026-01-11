package com.ecommerce.backend.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardResponse {
    private Long id;
    private String maskedCardNumber; // **** **** **** 1234
    private String expireDate;
}