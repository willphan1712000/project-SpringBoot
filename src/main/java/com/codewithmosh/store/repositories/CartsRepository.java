package com.codewithmosh.store.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithmosh.store.entities.Cart;

public interface CartsRepository extends JpaRepository<Cart, UUID> {}