package com.ecommerce.backend.services.abstracts;

public interface VerifyMailService {
    boolean verifyToken(String token);

}