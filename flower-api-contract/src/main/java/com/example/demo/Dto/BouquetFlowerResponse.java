package com.example.demo.Dto;

import java.math.BigDecimal;

public record BouquetFlowerResponse(
        Long flowerId,
        String flowerName,
        String flowerColor,
        Integer quantity,
        BigDecimal flowerPrice,
        BigDecimal subtotal
) {}
