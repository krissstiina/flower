package com.example.demo.endpoints;

import com.example.demo.Dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "flowers", description = "API для работы с цветами")
@RequestMapping("/api/flowers")
public interface FlowerApi {

    @Operation(summary = "Получить все цветы")
    @ApiResponse(responseCode = "200", description = "Список цветов")
    @GetMapping
    CollectionModel<EntityModel<FlowerResponse>> getAllFlowers();

    @Operation(summary = "Получить цветок по ID")
    @ApiResponse(responseCode = "200", description = "Цветок найден")
    @ApiResponse(responseCode = "404", description = "Цветок не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @GetMapping("/{id}")
    EntityModel<FlowerResponse> getFlowerById(@PathVariable Long id);

    @Operation(summary = "Обновить количество цветков на складе")
    @ApiResponse(responseCode = "200", description = "Количество обновлено")
    @ApiResponse(responseCode = "400", description = "Невалидные данные")
    @ApiResponse(responseCode = "404", description = "Цветок не найден")
    @PatchMapping("/{id}/stock")
    EntityModel<FlowerResponse> updateFlowerStock(@PathVariable Long id, @Valid @RequestBody UpdateStockRequest request);

    @Operation(summary = "Создать новый цветок")
    @ApiResponse(responseCode = "201", description = "Цветок успешно создан")
    @ApiResponse(responseCode = "400", description = "Невалидный запрос", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<FlowerResponse>> createFlower(@Valid @RequestBody FlowerRequest request);

    @Operation(summary = "Обновить цветок")
    @ApiResponse(responseCode = "200", description = "Цветок обновлен")
    @ApiResponse(responseCode = "404", description = "Цветок не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PutMapping("/{id}")
    EntityModel<FlowerResponse> updateFlower(@PathVariable Long id, @Valid @RequestBody UpdateFlowerRequest request);

    @Operation(summary = "Удалить цветок")
    @ApiResponse(responseCode = "204", description = "Цветок удален")
    @ApiResponse(responseCode = "404", description = "Цветок не найден")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteFlower(@PathVariable Long id);
}