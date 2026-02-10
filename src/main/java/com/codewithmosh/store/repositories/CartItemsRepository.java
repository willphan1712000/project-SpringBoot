package com.codewithmosh.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codewithmosh.store.entities.CartItem;

public interface CartItemsRepository extends JpaRepository<CartItem, Long> {
    @Query("select c from CartItem c where product.id = :id")
    CartItem findByProductId(@Param("id") Long id);
}