package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.dtos.requests.AddressRequestDTO;
import com.ecommerce.backend.dtos.responses.AddressResponseDTO;
import com.ecommerce.backend.entities.Address;
import com.ecommerce.backend.entities.User;
import com.ecommerce.backend.mappers.AddressMapper;
import com.ecommerce.backend.repositories.AddressRepository;
import com.ecommerce.backend.repositories.UserRepository;
import com.ecommerce.backend.services.abstracts.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;
    @Override
    public AddressResponseDTO createAddress(AddressRequestDTO addressRequest, Long userId) {
        Address address = addressMapper.addressFromRequest(addressRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        address.setUser(user);
        Address savedAddress = addressRepository.save(address);

        return addressMapper.responseFromAddress(savedAddress);
    }

    @Override
    public AddressResponseDTO updateAddress(Long addressId, AddressRequestDTO addressRequest) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setStreet(addressRequest.getStreet());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setZipCode(addressRequest.getZipCode());
        address.setCountry(addressRequest.getCountry());

        Address updatedAddress = addressRepository.save(address);
        return addressMapper.responseFromAddress(updatedAddress);
    }

    @Override
    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        addressRepository.delete(address);
    }

    @Override
    public List<AddressResponseDTO> getUserAddresses(Long userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);

        return addresses.stream()
                .map(addressMapper::responseFromAddress)
                .collect(Collectors.toList());
    }
}
