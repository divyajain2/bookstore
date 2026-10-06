package com.bookstore.exception;

import com.bookstore.domain.enums.OrderStatus;

public class InvalidOrderStateException extends RuntimeException {

    public InvalidOrderStateException(String orderNumber, OrderStatus currentStatus, String operation) {
        super(String.format("Cannot %s order %s: current status is %s", operation, orderNumber, currentStatus));
    }

    public InvalidOrderStateException(String message) {
        super(message);
    }
}