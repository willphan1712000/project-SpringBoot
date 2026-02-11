package com.codewithmosh.store.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import com.codewithmosh.store.exceptions.CartItemNotFoundException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.services.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Carts")
public class CartsController {
    private final CartService cartService;

    /**
     * Creating a Cart
     * @return
    */
   @PostMapping
    public ResponseEntity<CartDto> createCart() {
        var cartDto = cartService.createCart();
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    /**
     * Adding a Product to the Cart
     * @param cartId
     * @param request
     * @return
     */
    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add a product to a cart")
    public ResponseEntity<CartItemDto> addProductToCart(@Parameter(deprecated = true, description = "cart id") @PathVariable UUID cartId, @Valid @RequestBody AddCartItemDto request) {
        var cartItemDto = cartService.addProductToCart(cartId, request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    /**
     * Getting a Cart
     * @param cartId
     * @return
     */
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        var cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok(cartDto);
    }

    /**
     * Updating a Cart Item
     * @param cartId
     * @param productId
     * @param request
     * @return
     */
    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDto> updateCart(@PathVariable UUID cartId, @PathVariable Long productId, @Valid @RequestBody UpdateCartItemDto request ) {
        var cartItemDto = cartService.updateCart(cartId, productId, request.getQuantity());
        return ResponseEntity.ok(cartItemDto);
    }

    /**
     * Removing a Product from the Cart
     * @param cartId
     * @param productId
     * @return
     */
    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeCartItem(@PathVariable UUID cartId, @PathVariable Long productId) {
        cartService.deleteProductFromCart(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Clearing a cart -> remove every product in a cart
     * @param cartId
     * @return
    */
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> removeCart(@PathVariable UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> cartNotFoundHandler() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "cart not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> productNotFoundHandler() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "product not found"));
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<Map<String, String>> cartItemNotFoundHandler() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "cart item not found"));
    }
}