package com.example.demo.exseption;

public class InvalidOrderStatusException extends RuntimeException {

    public InvalidOrderStatusException(String message) {
        super(message);
    }
    public InvalidOrderStatusException(String currentStatus, String newStatus) {
        super(String.format("Cannot change order status from %s to %s", currentStatus, newStatus));
    }
}
