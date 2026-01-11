package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.core.utils.exceptions.ResourceNotFoundException;
import com.ecommerce.backend.dtos.requests.ProductRequestDTO;
import com.ecommerce.backend.dtos.responses.ProductResponseDTO;
import com.ecommerce.backend.entities.Category;
import com.ecommerce.backend.entities.Meta;
import com.ecommerce.backend.entities.Product;
import com.ecommerce.backend.mappers.ProductMapper;
import com.ecommerce.backend.repositories.CategoryRepository;
import com.ecommerce.backend.repositories.ProductRepository;
import com.ecommerce.backend.services.abstracts.ProductService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ecommerce.backend.repositories.CartItemRepository;

import java.time.Instant;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final CartItemRepository cartItemRepository;
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::responseFromProduct);
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return productMapper.responseFromProduct(product);
    }

    public ProductResponseDTO createProduct(ProductRequestDTO dto) {

        // Category bulunuyor
        Category category = categoryRepository
                .findBySlug(dto.getCategorySlug())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // Mapper ile Product oluşturuluyor
        Product product = productMapper.productFromRequest(dto, category);
        product.setCategory(category); // <-- ZORUNLU
        product.setId(null);
        product.setVersion(null);

        // Meta set ediliyor
        Meta meta = new Meta();
        meta.setCreatedAt(Instant.now());
        meta.setUpdatedAt(Instant.now());
        product.setMeta(meta);

        // Kaydet ve response döndür
        return productMapper.responseFromProduct(productRepository.save(product));
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Category bulunuyor
        Category category = categoryRepository
                .findBySlug(dto.getCategorySlug())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // Mapper ile güncelle
        productMapper.updateProductFromRequest(dto, product, category);

        // Meta güncelle
        if (product.getMeta() != null) {
            product.getMeta().setUpdatedAt(Instant.now());
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.responseFromProduct(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        cartItemRepository.deleteByProductId(id); // önce
        productRepository.deleteById(id);         // sonra
    }


    public ProductResponseDTO updateStock(Long id, Integer stock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setStock(stock);

        if (product.getMeta() != null) {
            product.getMeta().setUpdatedAt(Instant.now());
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.responseFromProduct(updatedProduct);
    }

    public Page<ProductResponseDTO> getProductsByCategory(String slug, Pageable pageable) {

        // Category bulunuyor
        Category category = categoryRepository
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        return productRepository.findByCategory(category, pageable)
                .map(productMapper::responseFromProduct);
    }
}

