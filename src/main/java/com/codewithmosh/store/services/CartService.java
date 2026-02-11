package com.codewithmosh.store.services;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codewithmosh.store.dtos.cart.CartDto;
import com.codewithmosh.store.dtos.cartItems.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.exceptions.CartItemNotFoundException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartItemMapper;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartsRepository;
import com.codewithmosh.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartService {
    private final CartsRepository cartsRepository;
    private final ProductRepository productRepository;

    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    public CartDto createCart() {
        var cart = new Cart();
        cartsRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartItemDto addProductToCart(UUID cartId, Long productId) {
        // Check if cart exists
        var cart = cartsRepository.findById(Objects.requireNonNull(cartId)).orElse(null);
        if(cart == null) {
            throw new CartNotFoundException();
        }

        // Check if product exists
        var product = productRepository.findById(Objects.requireNonNull(productId)).orElse(null);
        if(product == null) {
            throw new ProductNotFoundException(); // this comes from the body not from the request url
        }

        var cartItem = cart.addItem(product);

        cartsRepository.save(cart);

        return cartItemMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        var cart = cartsRepository.findById(Objects.requireNonNull(cartId)).orElse(null);
        if(cart == null) {
            throw new CartNotFoundException();
        }

        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCart(UUID cartId, Long productId, Integer quantity) {
        // Check if cart exists
        var cart = cartsRepository.findById(Objects.requireNonNull(cartId)).orElse(null);
        if(cart == null) {
            throw new CartNotFoundException();
        }

        // Check if cart item exists
        var cartItem = cart.findCartItem(productId);
        if(cartItem == null) {
            throw new CartItemNotFoundException();
        }

        cartItem.setQuantity(quantity);

        cartsRepository.save(cart);

        return cartItemMapper.toDto(cartItem);
    }

    public void deleteProductFromCart(UUID cartId, Long productId) {
        // Check if cart exists
        var cart = cartsRepository.findById(Objects.requireNonNull(cartId)).orElse(null);
        if(cart == null) {
            throw new CartNotFoundException();
        }

        // Check if cart item exists
        var cartItem = cart.findCartItem(productId);
        if(cartItem == null) {
            throw new CartItemNotFoundException();
        }

        cart.removeItem(cartItem);
        cartsRepository.save(cart);
    }

    public void clearCart(UUID cartId) {
        // Check if cart exists
        var cart = cartsRepository.findById(Objects.requireNonNull(cartId)).orElse(null);
        if(cart == null) {
            throw new CartNotFoundException();
        }

        cart.clear();
        cartsRepository.save(cart);
    }
}
