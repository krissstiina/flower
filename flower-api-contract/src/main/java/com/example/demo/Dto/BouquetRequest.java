package com.example.demo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BouquetRequest(
        @NotBlank(message = "Название букета не может быть пустым")
        String name,

        @NotNull(message = "Количество на складе не может быть пустым")
        @Positive(message = "Количество на складе должно быть положительным")
        Integer stockQuantity,

        @Size(max = 500, message = "Описание не может превышать 500 символов")
        String description,

        @NotNull(message = "Список цветов не может быть пустым")
        @Size(min = 1, message = "Букет должен содержать хотя бы один цветок")
        List<BouquetFlowerItem> flowers
) {}
