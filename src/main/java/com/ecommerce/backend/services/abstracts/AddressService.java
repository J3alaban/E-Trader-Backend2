package com.ecommerce.backend.services.abstracts;

import com.ecommerce.backend.dtos.requests.AddressRequestDTO;
import com.ecommerce.backend.dtos.responses.AddressResponseDTO;

import java.util.List;

public interface AddressService {
    AddressResponseDTO createAddress(AddressRequestDTO addressRequest, Long userId);
    AddressResponseDTO updateAddress(Long addressId, AddressRequestDTO addressRequest);
    void deleteAddress(Long addressId);
    List<AddressResponseDTO> getUserAddresses(Long userId);
}
