package com.example.demo.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateStockRequest(
        @NotNull(message = "Количество не может быть пустым")
        @PositiveOrZero(message = "Количество должно быть положительным или нулем")
        Integer stockQuantity
) {}
