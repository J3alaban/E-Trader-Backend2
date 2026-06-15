package com.debit.backend.services.concretes;

import com.debit.backend.core.utils.exceptions.ResourceNotFoundException;
import com.debit.backend.dtos.requests.ProductRequestDTO;
import com.debit.backend.dtos.responses.ProductResponseDTO;
import com.debit.backend.entities.Category;
import com.debit.backend.entities.Product;
import com.debit.backend.entities.SubCategories;
import com.debit.backend.entities.User;
import com.debit.backend.mappers.ProductMapper;
import com.debit.backend.repositories.CategoryRepository;
import com.debit.backend.repositories.ProductRepository;
import com.debit.backend.repositories.SubCategoryRepository;
import com.debit.backend.repositories.UserRepository;
import com.debit.backend.services.abstracts.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final UserRepository userRepository; // 🔥 EKLENDİ

    @Override
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::responseFromProduct);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return productMapper.responseFromProduct(product);
    }

    @Override
    public ProductResponseDTO createProduct(Long userId, ProductRequestDTO dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        SubCategories subCategory = null;

        if (dto.getSubCategoryId() != null) {
            subCategory = subCategoryRepository.findById(dto.getSubCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));

            if (!subCategory.getCategory().getId().equals(category.getId())) {
                throw new ResourceNotFoundException("SubCategory does not belong to Category");
            }
        }

        Product product = productMapper.productFromRequest(dto);

        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setUser(user);

        return productMapper.responseFromProduct(
                productRepository.save(product)
        );
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        SubCategories subCategory = null;

        if (dto.getSubCategoryId() != null) {
            subCategory = subCategoryRepository.findById(dto.getSubCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));

            if (!subCategory.getCategory().getId().equals(category.getId())) {
                throw new ResourceNotFoundException("SubCategory does not belong to Category");
            }
        }

        productMapper.update(dto, product, category, subCategory);

        return productMapper.responseFromProduct(
                productRepository.save(product)
        );
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponseDTO updateStock(Long id, Integer stock) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setStock(stock);

        return productMapper.responseFromProduct(
                productRepository.save(product)
        );
    }



    @Override
    public Page<ProductResponseDTO> getProductsByCategory(Long categoryId, Pageable pageable) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        return productRepository.findByCategory(category, pageable)
                .map(productMapper::responseFromProduct);
    }

    @Override
    public Page<ProductResponseDTO> getProductsBySubCategory(Long subCategoryId, Pageable pageable) {

        SubCategories subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found"));

        return productRepository.findBySubCategory(subCategory, pageable)
                .map(productMapper::responseFromProduct);
    }
}