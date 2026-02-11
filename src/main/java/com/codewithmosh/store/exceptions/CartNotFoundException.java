package com.codewithmosh.store.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException() {
        super();
    }
    
    public CartNotFoundException(String message) {
        super(message);
    }
}
