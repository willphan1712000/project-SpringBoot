package com.codewithmosh.store.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "carts")
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created", updatable = false, insertable = false)
    private LocalDateTime dateCreated;

    @OneToMany(mappedBy = "cart", cascade = { CascadeType.MERGE, CascadeType.REMOVE } , orphanRemoval = true, fetch = FetchType.EAGER )
    private Set<CartItem> cartItems;

    public void removeItem(CartItem item) {
        if(item == null) return;

        cartItems.remove(item);
        item.setCart(null);
    }

    public void clear() {
        cartItems.clear();
    }

    public BigDecimal getTotalPrice() {
        if (cartItems == null) {
            return BigDecimal.ZERO;
        }
        return cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CartItem findCartItem(Long productId) {
        return cartItems.stream().filter(
            item -> item.getProduct().getId().equals(productId)
        ).findFirst().orElse(null);
    }

    public CartItem addItem(Product product) {
        var cartItem = findCartItem(product.getId());

        if(cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1); // increase quantity of an existing product in cart item
        } else {
            cartItem = new CartItem();
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            cartItem.setProduct(product);
            cartItems.add(cartItem);
        }

        return cartItem;
    }
}