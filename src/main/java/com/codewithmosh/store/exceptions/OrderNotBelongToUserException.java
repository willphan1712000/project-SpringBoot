package com.codewithmosh.store.exceptions;

public class OrderNotBelongToUserException extends RuntimeException {
    public OrderNotBelongToUserException() {
        super("order not belong to user");
    }
}
