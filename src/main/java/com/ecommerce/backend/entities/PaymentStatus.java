package com.ecommerce.backend.entities;

public enum PaymentStatus {
    PENDING,        // Ödeme başlatıldı
    PAID,           // Ödeme başarılı
    FAILED,         // Ödeme başarısız
    CANCELED,       // Kullanıcı / sistem iptali
    REFUNDED        // İade edildi
}