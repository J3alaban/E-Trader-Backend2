package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.dtos.requests.CartItemRequest;
import com.ecommerce.backend.dtos.responses.CartResponse;
import com.ecommerce.backend.entities.Cart;
import com.ecommerce.backend.entities.CartItem;
import com.ecommerce.backend.entities.Product;
import com.ecommerce.backend.entities.User;
import com.ecommerce.backend.mappers.CartMapper;
import com.ecommerce.backend.repositories.CartRepository;
import com.ecommerce.backend.repositories.ProductRepository;
import com.ecommerce.backend.repositories.UserRepository;
import com.ecommerce.backend.services.abstracts.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    public CartResponse getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCart(userId));

        return buildResponse(cart);
    }

    @Override
    public CartResponse addItemToCart(Long userId, CartItemRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCart(userId));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow();

        CartItem item = cart.getCartItems()
                .stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            cart.getCartItems().add(item);
        } else {
            item.setQuantity(item.getQuantity() + request.getQuantity());
        }

        cartRepository.save(cart);
        return buildResponse(cart);
    }

    @Override
    public CartResponse removeItemFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow();

        cart.getCartItems()
                .removeIf(i -> i.getProduct().getId().equals(productId));

        cartRepository.save(cart);
        return buildResponse(cart);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow();

        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    private Cart createCart(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>());

        return cartRepository.save(cart);
    }

    private CartResponse buildResponse(Cart cart) {
        CartResponse response = cartMapper.toResponse(cart);

        double totalPrice = cart.getCartItems()
                .stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();

        response.setTotalPrice(totalPrice);
        return response;
    }
}