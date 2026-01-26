package com.ecommerce.backend.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusRequestDTO {
    private String status; // Örnek: Siparişin durumu (e.g. "PENDING", "SHIPPED", "DELIVERED", vs.)
}
