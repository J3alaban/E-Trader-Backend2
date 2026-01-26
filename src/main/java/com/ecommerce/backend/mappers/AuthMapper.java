package com.ecommerce.backend.mappers;

import com.ecommerce.backend.dtos.requests.RegisterUserRequestDTO;
import com.ecommerce.backend.dtos.requests.UserRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    UserRequestDTO addRequestFromRegisterRequest(RegisterUserRequestDTO request);
}
