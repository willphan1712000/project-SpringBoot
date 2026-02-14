package com.codewithmosh.store.exceptions;

public class CartNotFoundException extends RuntimeException {    
    public CartNotFoundException() {
        super("cart not found");
    }
}
