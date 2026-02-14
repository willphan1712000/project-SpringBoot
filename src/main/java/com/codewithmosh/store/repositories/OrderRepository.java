package com.codewithmosh.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithmosh.store.entities.orders.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {}
