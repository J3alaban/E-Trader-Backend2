package com.ecommerce.backend.mappers;

import com.ecommerce.backend.dtos.requests.DimensionsRequest;
import com.ecommerce.backend.dtos.requests.ProductRequestDTO;
import com.ecommerce.backend.dtos.responses.DimensionsResponse;
import com.ecommerce.backend.dtos.responses.ProductResponseDTO;
import com.ecommerce.backend.entities.Category;
import com.ecommerce.backend.entities.Dimensions;
import com.ecommerce.backend.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {DimensionsMapper.class, ReviewMapper.class, MetaMapper.class})
public interface ProductMapper {

    // ProductRequestDTO + Category → Product
    Product productFromRequest(ProductRequestDTO request, Category category);

    // Product → ProductResponseDTO
    @Mapping(target = "category", source = "category.slug")
    ProductResponseDTO responseFromProduct(Product product);

    // Update Product from Request + Category → default metod olarak implement
    default void updateProductFromRequest(ProductRequestDTO request, Product product, Category category) {
        if (request == null) return;

        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        if (request.getStock() != null) product.setStock(request.getStock());
        product.setCategory(category);
        product.setBrand(request.getBrand());
        product.setSku(request.getSku());
        product.setDiscountPercentage(request.getDiscountPercentage());
        product.setWeight(request.getWeight());
        product.setSize(request.getSize());
        product.setDimensions(new DimensionsMapper() {
            @Override
            public Dimensions toEntity(DimensionsRequest request) {
                return null;
            }

            @Override
            public DimensionsResponse toResponse(Dimensions dimensions) {
                return null;
            }
        }.toEntity(request.getDimensions()));

        if (product.getImages() != null) {
            List<String> list = request.getImages();
            if (list != null) {
                product.getImages().clear();
                product.getImages().addAll(list);
            } else {
                product.setImages(null);
            }
        } else {
            List<String> list = request.getImages();
            if (list != null) {
                product.setImages(new ArrayList<>(list));
            }
        }
    }
}
