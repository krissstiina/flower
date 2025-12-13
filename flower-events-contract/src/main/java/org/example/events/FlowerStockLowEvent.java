package org.example.events;

import java.io.Serializable;
import java.time.LocalDateTime;

public record FlowerStockLowEvent(
        Long flowerId,
        String flowerName,
        String flowerColor,
        Integer currentStock,
        Integer minimumStockThreshold,
        LocalDateTime alertTime
) implements Serializable {}
