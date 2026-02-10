package com.codewithmosh.store.controllers;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.codewithmosh.store.repositories.CartItemsRepository;
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
    private final CartItemsRepository cartItemsRepository;

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
    public ResponseEntity<CartItemDto> addProductToCart(@PathVariable UUID cartId, @RequestBody AddCartItemDto request) {
        // Check if cart exists
        var cart = cartsRepository.findById(Objects.requireNonNull(cartId)).orElse(null);
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }

        var productId = Objects.requireNonNull(request.getProductId()); // get product id
        // Check if product exists
        var product = productRepository.findById(productId).orElse(null);
        if(product == null) {
            return ResponseEntity.badRequest().build();
        }

        // Check if there is existing product in cart item
        var cartItem = cartItemsRepository.findByProductId(productId);
        if(cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity()); // increase quantity of an existing product in cart item
        } else {
            cartItem = cartItemMapper.toEntity(request);
            cartItem.setCart(cart);
            cartItem.setProduct(product);
        }

        cartItemsRepository.save(cartItem);

        var cartItemDto = cartItemMapper.toDto(cartItem);
        cartItemDto.setTotalPrice(
            product.getPrice().multiply(BigDecimal.valueOf(
                cartItem.getQuantity()
            ))
        );

        return new ResponseEntity<>(cartItemDto, HttpStatus.CREATED);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        var cart = cartsRepository.findById(Objects.requireNonNull(cartId)).orElse(null);
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }

        var cartDto = cartMapper.toDto(cart);

        cartDto.getItems().forEach(item -> {
            item.setTotalPrice(
                item.getProduct().getPrice().multiply(
                    BigDecimal.valueOf(
                        item.getQuantity()
                    )
                )
            );

            cartDto.setTotalPrice(
                cartDto.getTotalPrice().add(item.getTotalPrice())
            );
        });

        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDto> updateCart(@PathVariable UUID cartId, @PathVariable Long productId, @Valid @RequestBody UpdateCartItemDto request ) {
        // Check if cart exists
        var cart = cartsRepository.findById(Objects.requireNonNull(cartId));
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if cart item exists
        var cartItem = cartItemsRepository.findByProductId(productId);
        if(cartItem == null) {
            return ResponseEntity.notFound().build();
        }

        cartItem.setQuantity(
            request.getQuantity()
        );

        cartItemsRepository.save(cartItem);

        var cartItemDto = cartItemMapper.toDto(cartItem);
        cartItemDto.setTotalPrice(
            cartItemDto.getProduct().getPrice().multiply(
                BigDecimal.valueOf(
                    cartItemDto.getQuantity()
                )
            )
        );

        return ResponseEntity.ok(cartItemDto);
    }
}