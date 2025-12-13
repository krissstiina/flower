package com.example.demo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateFlowerRequest(
        @NotBlank(message = "Название цветка не может быть пустым")
        String name,

        @NotBlank(message = "Цвет не может быть пустым")
        String color,

        @NotNull(message = "Цена не может быть пустой")
        @Positive(message = "Цена должна быть положительной")
        BigDecimal price,

        @NotNull(message = "Количество на складе не может быть пустым")
        @Positive(message = "Количество на складе должно быть положительным")
        Integer stockQuantity,

        @Size(max = 500, message = "Описание не может превышать 500 символов")
        String description
) {}