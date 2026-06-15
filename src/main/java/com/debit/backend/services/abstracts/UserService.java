package com.debit.backend.services.abstracts;

import com.debit.backend.dtos.requests.LoginRequestDTO;
import com.debit.backend.dtos.requests.UserRequestDTO;
import com.debit.backend.dtos.responses.ProductResponseDTO;
import com.debit.backend.dtos.responses.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserResponseDTO registerUser(UserRequestDTO userRequestDTO);

    UserResponseDTO loginUser(LoginRequestDTO loginRequestDTO);

    UserResponseDTO getUserProfile(Long userId);

    UserResponseDTO updateUserProfile(Long userId, UserRequestDTO userRequestDTO);

    List<UserResponseDTO> getAllUsers();

    Page<ProductResponseDTO> getUserProducts(Long userId, Pageable pageable);
}