package com.ecommerce.backend.services.abstracts;

import com.ecommerce.backend.dtos.requests.CardRequest;
import com.ecommerce.backend.dtos.responses.CardResponse;
import com.ecommerce.backend.entities.Card;

import java.util.List;

public interface CardService {

    void save(Long userId, CardRequest request);

    void update(Long cardId, CardRequest request);

    void delete(Long cardId);

    List<CardResponse> getUserCards(Long userId);
}