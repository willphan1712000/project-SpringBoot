package com.codewithmosh.store.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.entities.orders.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "orderItems.product")
    @Query("select o from Order o where o.customer = :customer")
    List<Order> getAllByCustomer(@Param("customer") User customer);
}
