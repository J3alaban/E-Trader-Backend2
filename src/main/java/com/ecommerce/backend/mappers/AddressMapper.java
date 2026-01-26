package com.ecommerce.backend.mappers;

import com.ecommerce.backend.dtos.requests.AddressRequestDTO;
import com.ecommerce.backend.dtos.responses.AddressResponseDTO;
import com.ecommerce.backend.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {


    Address addressFromRequest(AddressRequestDTO request);
    //Address addressFromRequest(AddressRequestDTO request);

    @Mapping(target = "userId", source = "user.id")
    AddressResponseDTO responseFromAddress(Address address);
}
