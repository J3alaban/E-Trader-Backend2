package com.debit.backend.mappers;

import com.debit.backend.dtos.requests.UserRequestDTO;
import com.debit.backend.dtos.responses.UserResponseDTO;
import com.debit.backend.dtos.responses.RegisterUserResponseDTO;
import com.debit.backend.entities.User;
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

    UserResponseDTO toResponseDTO(User user);





    // PROFILE / ME
    @Mapping(source = "phone", target = "phone")
    UserResponseDTO responseFromUser(User user);

    @Mapping(source = "tcNo", target = "tcNo")
    RegisterUserResponseDTO registerResponseFromUser(User user);
}
