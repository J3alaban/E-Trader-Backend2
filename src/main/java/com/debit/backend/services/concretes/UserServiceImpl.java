package com.debit.backend.services.concretes;

import com.debit.backend.core.services.JwtService;
import com.debit.backend.core.utils.exceptions.ResourceNotFoundException;
import com.debit.backend.dtos.requests.LoginRequestDTO;
import com.debit.backend.dtos.requests.UserRequestDTO;
import com.debit.backend.dtos.responses.ProductResponseDTO;
import com.debit.backend.dtos.responses.UserResponseDTO;
import com.debit.backend.entities.EmailVerificationToken;
import com.debit.backend.entities.Role;
import com.debit.backend.entities.User;
import com.debit.backend.mappers.ProductMapper;
import com.debit.backend.mappers.UserMapper;
import com.debit.backend.repositories.EmailVerificationTokenRepository;
import com.debit.backend.repositories.ProductRepository;
import com.debit.backend.repositories.UserRepository;
import com.debit.backend.services.MailService;
import com.debit.backend.services.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository; // 🔥 EKLENDİ
    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailService mailService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

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
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);

        EmailVerificationToken token = new EmailVerificationToken();
        token.setUser(savedUser);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        token.setUsed(false);

        emailVerificationTokenRepository.save(token);

        mailService.sendVerificationMail(savedUser.getEmail(), token.getToken());

        return userMapper.responseFromUser(savedUser);
    }

    @Override
    public Page<ProductResponseDTO> getUserProducts(Long userId, Pageable pageable) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return productRepository.findByUser(user, pageable)
                .map(productMapper::responseFromProduct);
    }

    @Override
    public UserResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {

        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        if (!user.isEmailVerified()) {
            throw new IllegalStateException("Email not verified");
        }

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        String token = jwtService.generateToken(userDetails);

        UserResponseDTO responseDTO = userMapper.responseFromUser(user);
        responseDTO.setToken(token);

        return responseDTO;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::responseFromUser)
                .toList();
    }

    @Override
    public UserResponseDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userMapper.responseFromUser(user);
    }

    @Override
    public UserResponseDTO updateUserProfile(Long userId, UserRequestDTO userRequestDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userMapper.updateUserFromRequest(userRequestDTO, user);

        if (userRequestDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        return userMapper.responseFromUser(userRepository.save(user));
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}