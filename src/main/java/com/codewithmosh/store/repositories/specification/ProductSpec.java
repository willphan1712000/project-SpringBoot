package com.codewithmosh.store.repositories.specification;

import org.springframework.data.jpa.domain.Specification;

import com.codewithmosh.store.entities.Product;

import jakarta.persistence.criteria.JoinType;

public class ProductSpec {
    public static Specification<Product> hasCategoryEqualTo(Byte categoryId) {
        return (root, query, cb) -> cb.equal(root.join("category", JoinType.LEFT).get("id"), categoryId);
    }
}
