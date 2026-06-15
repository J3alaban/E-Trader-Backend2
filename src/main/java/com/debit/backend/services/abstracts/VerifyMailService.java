package com.debit.backend.services.abstracts;

public interface VerifyMailService {
    boolean verifyToken(String token);

}