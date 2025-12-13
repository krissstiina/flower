package com.example.demo.exseption;

public class OrderAlreadyCancelledException extends RuntimeException {
    public OrderAlreadyCancelledException(Long orderId) {
        super(String.format("Order with id=%s is already cancelled", orderId));
    }
}
