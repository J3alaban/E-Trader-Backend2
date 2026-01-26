package com.ecommerce.backend.controller;

import com.ecommerce.backend.dtos.requests.AddressRequestDTO;
import com.ecommerce.backend.dtos.responses.AddressResponseDTO;
import com.ecommerce.backend.services.abstracts.AddressService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/addresses")
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponseDTO> create(
            @PathVariable Long userId,
            @RequestBody @Valid AddressRequestDTO dto
    ) {
        return ResponseEntity.ok(addressService.createAddress(dto, userId));
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getUserAddresses(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(addressService.getUserAddresses(userId));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> update(
            @PathVariable Long addressId,
            @RequestBody @Valid AddressRequestDTO dto
    ) {
        return ResponseEntity.ok(addressService.updateAddress(addressId, dto));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> delete(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
