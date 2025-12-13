package com.example.demo.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BouquetFlowerItem(
        @NotNull(message = "ID цветка не может быть пустым")
        Long flowerId,

        @NotNull(message = "Количество цветков не может быть пустым")
        @Positive(message = "Количество цветков должно быть положительным")
        Integer quantity
) {}
