package com.example.demo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderRequest(
        @NotNull(message = "ID букета не может быть пустым")
        Long bouquetId,

        @NotNull(message = "Количество не может быть пустым")
        @Positive(message = "Количество должно быть положительным")
        Integer quantity,

        @NotBlank(message = "Адрес доставки не может быть пустым")
        String deliveryAddress

) {}

