package com.ecommerce.backend.mappers;
import com.ecommerce.backend.dtos.requests.PaymentRequest;
import com.ecommerce.backend.dtos.responses.PaymentResponse;
import com.ecommerce.backend.entities.Address;
import com.ecommerce.backend.entities.Order;
import com.ecommerce.backend.entities.Payment;
import com.ecommerce.backend.entities.PaymentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    // Entity -> ResponseDTO (Kullanıcıya güzel bir adres metni dönmek için)
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(expression = "java(formatFullAddress(payment.getAddress()))", target = "address")
    PaymentResponse toResponse(Payment payment);

    // Request + Entityler -> Payment Entity (Kaydetmek için)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", expression = "java(order)")
    @Mapping(target = "address", expression = "java(address)")
    @Mapping(target = "status", constant = "PENDING")
    Payment toEntity(PaymentRequest request, Order order, Address address);

    // Adres birleştirme mantığı (Default method)
    default String formatFullAddress(Address address) {
        if (address == null) return null;
        return String.format("%s, %s, %s / %s",
                address.getStreet(), address.getCity(), address.getState(), address.getCountry());
    }
}

