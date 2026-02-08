package com.codewithmosh.store.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.repositories.specification.ProductSpec;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping()
    public List<ProductDto> getAllProductsBy(@RequestParam(required =  false, name = "categoryId") Byte categoryId) {
        List<Product> products;
        if(categoryId == null) {
            products = productRepository.findAll();
        } else {
            products = productRepository.findAll(ProductSpec.hasCategoryEqualTo(categoryId));
        }

        return products
            .stream()
            .map(productMapper::toDto)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductBy(@PathVariable Long id) {
        if(id == null) {
            return ResponseEntity.notFound().build();
        }

        var product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productMapper.toDto(product));
    }
}
