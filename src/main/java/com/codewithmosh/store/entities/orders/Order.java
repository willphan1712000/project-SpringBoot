package com.codewithmosh.store.entities.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "customer_id")
    private Long customerId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    public void addItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }
    public void removeItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
    }

    public BigDecimal getTotalPrice() {
        return orderItems.stream().map(OrderItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
