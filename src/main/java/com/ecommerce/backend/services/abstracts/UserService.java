package com.ecommerce.backend.services.abstracts;

import com.ecommerce.backend.dtos.requests.AddressRequestDTO;
import com.ecommerce.backend.dtos.requests.LoginRequestDTO;
import com.ecommerce.backend.dtos.requests.UserRequestDTO;
import com.ecommerce.backend.dtos.responses.AddressResponseDTO;
import com.ecommerce.backend.dtos.responses.UserResponseDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserResponseDTO registerUser(UserRequestDTO userRequestDTO);

    UserResponseDTO loginUser(LoginRequestDTO loginRequestDTO);

    UserResponseDTO getUserProfile(Long userId);

    UserResponseDTO updateUserProfile(Long userId, UserRequestDTO userRequestDTO);

    AddressResponseDTO addUserAddress(Long userId, AddressRequestDTO addressRequestDTO);
}
