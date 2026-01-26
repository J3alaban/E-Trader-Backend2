package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.core.services.JwtService;
import com.ecommerce.backend.core.utils.exceptions.ResourceNotFoundException;
import com.ecommerce.backend.dtos.requests.AddressRequestDTO;
import com.ecommerce.backend.dtos.requests.LoginRequestDTO;
import com.ecommerce.backend.dtos.requests.UserRequestDTO;
import com.ecommerce.backend.dtos.responses.AddressResponseDTO;
import com.ecommerce.backend.dtos.responses.UserResponseDTO;
import com.ecommerce.backend.entities.*;
import com.ecommerce.backend.mappers.AddressMapper;
import com.ecommerce.backend.mappers.UserMapper;
import com.ecommerce.backend.repositories.AddressRepository;
import com.ecommerce.backend.repositories.EmailVerificationTokenRepository;
import com.ecommerce.backend.repositories.UserRepository;
import com.ecommerce.backend.services.abstracts.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.ecommerce.backend.services.MailService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    private final MailService mailService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;



    /****/

    private String encodePasswordIfNeeded(String rawPassword) {
        if (rawPassword == null) return null;
        // BCrypt hash kontrolü: $2a$ ile başlıyorsa zaten hashlenmiş
        if (rawPassword.startsWith("$2a$")) {
            return rawPassword;
        }
        return passwordEncoder.encode(rawPassword);
    }



    @Override
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already taken");
        }

        User user = new User();
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setPhone(userRequestDTO.getPhone());
        user.setRole(Role.CUSTOMER);
        user.setTcNo(userRequestDTO.getTcNo());
        user.setEmailVerified(false); // 🔹 eklendi

        User savedUser = userRepository.save(user);

        // 🔹 EMAIL VERIFICATION TOKEN OLUŞTUR
        EmailVerificationToken token = new EmailVerificationToken();
        token.setUser(savedUser);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        token.setUsed(false);

        emailVerificationTokenRepository.save(token);

        // 🔹 MAIL GÖNDER
        mailService.sendVerificationMail(savedUser.getEmail(), token.getToken());

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(savedUser.getId());
        responseDTO.setFirstName(savedUser.getFirstName());
        responseDTO.setLastName(savedUser.getLastName());
        responseDTO.setEmail(savedUser.getEmail());
        responseDTO.setPhone(savedUser.getPhone());
        responseDTO.setRole(savedUser.getRole().name());

        return responseDTO;
    }







    @Override
    public UserResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {

        // 🔹 EMAIL DOĞRULAMA KONTROLÜ (eklendi)
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        if (!user.isEmailVerified()) {
            throw new IllegalStateException("Email not verified");
        }

        // Şifre kontrolü
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        // UserDetails oluştur
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        // Token üret
        String token = jwtService.generateToken(userDetails);

        // Log
        System.out.println("User email: " + user.getEmail());
        System.out.println("User role: " + user.getRole());
        System.out.println("UserDetails username: " + userDetails.getUsername());
        System.out.println("Generated token: " + token);

        // DTO oluştur ve dön
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setFirstName(user.getFirstName());
        responseDTO.setLastName(user.getLastName());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setRole(user.getRole().name());
        responseDTO.setToken(token);

        return responseDTO;
    }




























    @Override
    public UserResponseDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.responseFromUser(user);
    }

    @Override
    public UserResponseDTO updateUserProfile(Long userId, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUserFromRequest(userRequestDTO, user);

        if (userRequestDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
            user.setPassword(encodePasswordIfNeeded(userRequestDTO.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return userMapper.responseFromUser(updatedUser);
    }

    @Override
    public AddressResponseDTO addUserAddress(Long userId, AddressRequestDTO addressRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = addressMapper.addressFromRequest(addressRequestDTO);
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);
        return addressMapper.responseFromAddress(savedAddress);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws ResourceNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}