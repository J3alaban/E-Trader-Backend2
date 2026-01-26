package com.ecommerce.backend.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,       // Hesapsız gezenler için (Genelde DB'de tutulmaz, default atanır)
    CUSTOMER,   // Kayıt olup giriş yapanlar
    ADMIN;      // Yetkili kişi

    @Override
    public String getAuthority() {
        return name();
    }
}
