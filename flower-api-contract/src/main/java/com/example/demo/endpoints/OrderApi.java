package com.example.demo.endpoints;

import com.example.demo.Dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "orders", description = "API для управления заказами")
@RequestMapping("/api/orders")
public interface OrderApi {

    @Operation(summary = "Получить все заказы")
    @ApiResponse(responseCode = "200", description = "Список заказов")
    @GetMapping
    CollectionModel<EntityModel<OrderResponse>> getAllOrders();

    @Operation(summary = "Получить заказ по ID")
    @ApiResponse(responseCode = "200", description = "Заказ найден")
    @ApiResponse(responseCode = "404", description = "Заказ не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @GetMapping("/{id}")
    EntityModel<OrderResponse> getOrderById(@PathVariable Long id);

    @Operation(summary = "Получить список всех заказов с пагинацией")
    @ApiResponse(responseCode = "200", description = "Список заказов")
    @GetMapping("/page")
    PagedModel<EntityModel<OrderResponse>> getAllOrders(
            @Parameter(description = "Номер страницы (0..N)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Получить заказы по статусу")
    @ApiResponse(responseCode = "200", description = "Список заказов по статусу")
    @GetMapping("/status/{status}")
    CollectionModel<EntityModel<OrderResponse>> getOrdersByStatus(@PathVariable String status);

    @Operation(summary = "Создать новый заказ")
    @ApiResponse(responseCode = "201", description = "Заказ успешно создан")
    @ApiResponse(responseCode = "400", description = "Невалидный запрос", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @ApiResponse(responseCode = "404", description = "Букет не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @ApiResponse(responseCode = "409", description = "Недостаточно товара на складе", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<OrderResponse>> createOrder(@Valid @RequestBody OrderRequest request);

    @Operation(summary = "Обновить статус заказа")
    @ApiResponse(responseCode = "200", description = "Статус заказа обновлен")
    @ApiResponse(responseCode = "404", description = "Заказ не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @ApiResponse(responseCode = "400", description = "Невалидный статус", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PutMapping("/{id}/status")
    EntityModel<OrderResponse> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusRequest request);

    @Operation(summary = "Обновить дату доставки заказа")
    @ApiResponse(responseCode = "200", description = "Дата доставки обновлена")
    @ApiResponse(responseCode = "404", description = "Заказ не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PatchMapping("/{id}/delivery-date")
    EntityModel<OrderResponse> updateDeliveryDate(@PathVariable Long id, @RequestParam LocalDateTime deliveryDate);

    @Operation(summary = "Отменить заказ")
    @ApiResponse(responseCode = "200", description = "Заказ отменен")
    @ApiResponse(responseCode = "404", description = "Заказ не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @ApiResponse(responseCode = "409", description = "Невозможно отменить заказ", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping("/{id}/cancel")
    EntityModel<OrderResponse> cancelOrder(@PathVariable Long id);

    @Operation(summary = "Удалить заказ")
    @ApiResponse(responseCode = "204", description = "Заказ удален")
    @ApiResponse(responseCode = "404", description = "Заказ не найден")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteOrder(@PathVariable Long id);
}