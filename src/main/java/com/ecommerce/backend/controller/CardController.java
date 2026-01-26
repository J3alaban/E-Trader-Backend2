package com.ecommerce.backend.controller;

import com.ecommerce.backend.dtos.requests.CardRequest;
import com.ecommerce.backend.dtos.responses.CardResponse;
import com.ecommerce.backend.services.abstracts.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    // Kullanıcıya kart ekleme
    @PostMapping("/{userId}")  // /api/v1/cards/{userId}/{cardId}
    public ResponseEntity<Void> addCard(
            @PathVariable Long userId,
            @RequestBody CardRequest request) {
        cardService.save(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Kart güncelleme
    @PutMapping("/{cardId}")   //   /{userId}/{cardId}
    public ResponseEntity<Void> updateCard(
            @PathVariable Long cardId,
            @RequestBody CardRequest request) {
        cardService.update(cardId, request);
        return ResponseEntity.ok().build();
    }

    // Kart silme
    @DeleteMapping("/{cardId}") ///  /{userId}/{cardId}
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.delete(cardId);
        return ResponseEntity.noContent().build();
    }

    // Kullanıcının kartlarını listeleme
    @GetMapping("/{userId}")
    public ResponseEntity<List<CardResponse>> getUserCards(@PathVariable Long userId) {
        List<CardResponse> cards = cardService.getUserCards(userId);
        return ResponseEntity.ok(cards);
    }
}