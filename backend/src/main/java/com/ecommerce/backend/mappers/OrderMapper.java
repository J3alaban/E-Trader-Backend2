package com.ecommerce.backend.mappers;

import com.ecommerce.backend.dtos.requests.OrderRequestDTO;
import com.ecommerce.backend.dtos.requests.OrderStatusRequestDTO;
import com.ecommerce.backend.dtos.responses.OrderResponseDTO;
import com.ecommerce.backend.entities.Order;
import com.ecommerce.backend.entities.Product;
import com.ecommerce.backend.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "address.id", source = "addressId")
    @Mapping(target = "products", source = "productId")  // productId listesini products listesine bağla
    Order orderFromRequest(OrderRequestDTO request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "addressId", source = "address.id")
    @Mapping(target = "products", source = "products") // products listesini map et
    OrderResponseDTO responseFromOrder(Order order);

    @Mapping(source = "status", target = "orderStatus")
    Order toEntity(OrderStatusRequestDTO orderStatusRequestDTO);

    @Mapping(source = "orderStatus", target = "status")
    OrderResponseDTO toResponseDTO(Order order);

    // Product ID'den Product nesnesi oluştur
    @Mapping(target = "id", source = "productId")
    Product mapProduct(Long productId);

    // Product ID listesini Product listesine çevir
    default List<Product> mapProductIdsToProducts(List<Long> productIds) {
        return productIds.stream()
                .map(this::mapProduct)
                .collect(Collectors.toList());
    }

    // Category -> String mapping
    default String map(Category category) {
        return category != null ? category.getSlug() : null;
    }

    // Product listesini String listesindeki kategorilere map et
    default List<String> mapProductsToCategorySlugs(List<Product> products) {
        return products.stream()
                .map(Product::getCategory)
                .map(this::map)
                .collect(Collectors.toList());
    }
}




