package com.example.demo.exseption;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String resourceName, Object resourceId, Integer available, Integer requested) {
        super(String.format("Insufficient stock for %s with id=%s. Available: %d, Requested: %d",
                resourceName, resourceId, available, requested));
    }
}
