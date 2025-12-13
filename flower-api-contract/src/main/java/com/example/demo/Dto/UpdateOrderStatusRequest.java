package com.example.demo.Dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateOrderStatusRequest(
        @NotBlank(message = "Статус не может быть пустым")
        String status
) {}
