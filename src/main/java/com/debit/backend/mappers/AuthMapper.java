package com.debit.backend.mappers;

import com.debit.backend.dtos.requests.RegisterUserRequestDTO;
import com.debit.backend.dtos.requests.UserRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    UserRequestDTO addRequestFromRegisterRequest(RegisterUserRequestDTO request);
}
