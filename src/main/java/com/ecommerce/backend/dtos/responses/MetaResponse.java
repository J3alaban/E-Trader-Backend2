package com.ecommerce.backend.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetaResponse {
    private String createdAt;
    private String updatedAt;
    private String barcode;
    private String qrCode;
}