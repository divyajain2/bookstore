package com.bookstore.exception;

public class InsufficientInventoryException extends RuntimeException {

    public InsufficientInventoryException(String isbn, int requested, int available) {
        super(String.format("Insufficient inventory for book %s: requested %d, available %d", isbn, requested, available));
    }
}