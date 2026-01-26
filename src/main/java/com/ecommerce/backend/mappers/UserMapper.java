package com.ecommerce.backend.mappers;

import com.ecommerce.backend.dtos.requests.UserRequestDTO;
import com.ecommerce.backend.dtos.responses.UserResponseDTO;
import com.ecommerce.backend.dtos.responses.RegisterUserResponseDTO;
import com.ecommerce.backend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    void updateUserFromRequest(UserRequestDTO request,
                               @MappingTarget User user);

    // REGISTER
    @Mapping(source = "phone", target = "phone")
    User userFromRequest(UserRequestDTO dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")







    // PROFILE / ME
    @Mapping(source = "phone", target = "phone")
    UserResponseDTO responseFromUser(User user);

    @Mapping(source = "tcNo", target = "tcNo")
    RegisterUserResponseDTO registerResponseFromUser(User user);
}
