package org.example.events;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDeliveredEvent(
        Long orderId,
        Long bouquetId,
        String bouquetName,
        String customerInfo,
        String deliveryAddress,
        Integer quantity,
        BigDecimal totalPrice,
        LocalDateTime orderDate,
        LocalDateTime deliveredAt
) implements Serializable {}
