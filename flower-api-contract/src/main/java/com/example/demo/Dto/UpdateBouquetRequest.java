package com.example.demo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateBouquetRequest(
        @NotBlank(message = "Название букета не может быть пустым")
        String name,

        @NotNull(message = "Количество на складе не может быть пустым")
        Integer stockQuantity,

        @Size(max = 500, message = "Описание не может превышать 500 символов")
        String description
) {}

