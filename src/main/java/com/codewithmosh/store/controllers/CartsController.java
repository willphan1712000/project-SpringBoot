package com.codewithmosh.store.controllers;

import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.dtos.cart.CartDto;
import com.codewithmosh.store.dtos.cartItems.AddCartItemDto;
import com.codewithmosh.store.dtos.cartItems.CartItemDto;
import com.codewithmosh.store.dtos.cartItems.UpdateCartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.mappers.CartItemMapper;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartsRepository;
import com.codewithmosh.store.repositories.ProductRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartsController {
    private final ProductRepository productRepository;
    private final CartsRepository cartsRepository;

    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    @PostMapping
    public ResponseEntity<CartDto> createCart() {
        var cart = new Cart();
        cartsRepository.save(cart);
        var cartDto = cartMapper.toDto(cart);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addProductToCart(@PathVariable UUID cartId, @Valid @RequestBody AddCartItemDto request) {
        // Check if cart exists
        var cart = cartsRepository.findById(Objects.requireNonNull(cartId)).orElse(null);
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }

        var productId = Objects.requireNonNull(request.getProductId()); // get product id
        // Check if product exists
        var product = productRepository.findById(productId).orElse(null);
        if(product == null) {
            return ResponseEntity.badRequest().build(); // this comes from the body not from the request url
        }

        var cartItem = cart.addItem(product);

        cartsRepository.save(cart);

        return new ResponseEntity<>(cartItemMapper.toDto(cartItem), HttpStatus.CREATED);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        var cart = cartsRepository.findById(Objects.requireNonNull(cartId)).orElse(null);
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDto> updateCart(@PathVariable UUID cartId, @PathVariable Long productId, @Valid @RequestBody UpdateCartItemDto request ) {
        // Check if cart exists
        var cart = cartsRepository.findById(Objects.requireNonNull(cartId)).orElse(null);
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if cart item exists
        var cartItem = cart.findCartItem(productId);

        cartItem.setQuantity(request.getQuantity());

        cartsRepository.save(cart);

        return ResponseEntity.ok(cartItemMapper.toDto(cartItem));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable UUID cartId, @PathVariable Long productId) {
        // Check if cart exists
        var cart = cartsRepository.findById(Objects.requireNonNull(cartId)).orElse(null);
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if cart item exists
        var cartItem = cart.findCartItem(productId);
        if(cartItem == null) {
            return ResponseEntity.notFound().build();
        }

        cart.removeItem(cartItem);
        cartsRepository.save(cart);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> removeCart(@PathVariable UUID cartId) {
        // Check if cart exists
        var cart = cartsRepository.findById(Objects.requireNonNull(cartId)).orElse(null);
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }

        cartsRepository.delete(cart);

        return ResponseEntity.noContent().build();
    }
}