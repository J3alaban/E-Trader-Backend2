package com.ecommerce.backend.controller;

import com.ecommerce.backend.core.services.JwtService;
import com.ecommerce.backend.core.utils.exceptions.ResourceNotFoundException;
import com.ecommerce.backend.dtos.requests.AddressRequestDTO;
import com.ecommerce.backend.dtos.requests.LoginRequestDTO;
import com.ecommerce.backend.dtos.requests.UserRequestDTO;
import com.ecommerce.backend.dtos.responses.AddressResponseDTO;
import com.ecommerce.backend.dtos.responses.LoginResponseDTO;
import com.ecommerce.backend.dtos.responses.UserResponseDTO;
import com.ecommerce.backend.entities.User;
import com.ecommerce.backend.repositories.UserRepository;
import com.ecommerce.backend.services.abstracts.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;












    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {

        try {

            UserResponseDTO userResponse = userService.loginUser(loginRequestDTO);


            LoginResponseDTO loginResponse = new LoginResponseDTO();
            loginResponse.setToken(userResponse.getToken());
            loginResponse.setId(userResponse.getId().toString());
            loginResponse.setRole(userResponse.getRole());

            return ResponseEntity.ok(loginResponse);

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO());

        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new LoginResponseDTO());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponseDTO());
        }



    }





    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        // Service katmanı artık otomatik olarak CUSTOMER rolünü setleyip dönecek
        UserResponseDTO response = userService.registerUser(userRequestDTO);


        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserResponseDTO> getUserProfile(@PathVariable Long userId) {
        UserResponseDTO responseDTO = userService.getUserProfile(userId);
        return ResponseEntity.ok(responseDTO);
    }


    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserResponseDTO> updateUserProfile(
            @PathVariable Long userId,
            @RequestBody UserRequestDTO userRequestDTO
    ) {
        UserResponseDTO responseDTO = userService.updateUserProfile(userId, userRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}