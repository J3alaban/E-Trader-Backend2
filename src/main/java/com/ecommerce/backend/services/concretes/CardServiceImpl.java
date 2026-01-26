package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.dtos.requests.CardRequest;
import com.ecommerce.backend.dtos.responses.CardResponse;
import com.ecommerce.backend.entities.Card;
import com.ecommerce.backend.repositories.CardRepository;
import com.ecommerce.backend.services.abstracts.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Override
    public void save(Long userId, CardRequest request) {
        Card card = new Card();
        card.setUserId(userId);
        card.setCardNumber(request.getCardNumber());
        card.setExpireDate(request.getExpireDate());
        card.setCvv(request.getCvv());
        cardRepository.save(card);
    }

    @Override
    public void update(Long cardId, CardRequest request) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        card.setCardNumber(request.getCardNumber());
        card.setExpireDate(request.getExpireDate());
        card.setCvv(request.getCvv());

        cardRepository.save(card);
    }

    @Override
    public void delete(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    @Override
    public List<CardResponse> getUserCards(Long userId) {
        return cardRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private CardResponse toResponse(Card card) {
        CardResponse response = new CardResponse();
        response.setId(card.getId());
        response.setExpireDate(card.getExpireDate());
        response.setMaskedCardNumber(
                "**** **** **** " +
                        card.getCardNumber()
                                .substring(card.getCardNumber().length() - 4)
        );
        return response;
    }
}