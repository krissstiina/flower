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

import java.util.List;

@Tag(name = "bouquets", description = "API для работы с букетами (составные)")
@RequestMapping("/api/bouquets")
public interface BouquetApi {

    @Operation(summary = "Получить все букеты")
    @ApiResponse(responseCode = "200", description = "Список букетов")
    @GetMapping
    CollectionModel<EntityModel<BouquetResponse>> getAllBouquets();

    @Operation(summary = "Получить букет по ID")
    @ApiResponse(responseCode = "200", description = "Букет найден")
    @ApiResponse(responseCode = "404", description = "Букет не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @GetMapping("/{id}")
    EntityModel<BouquetResponse> getBouquetById(@PathVariable Long id);

//    @Operation(summary = "Получить букеты с пагинацией")
//    @ApiResponse(responseCode = "200", description = "Список букетов")
//    @GetMapping("/page")
//    PagedModel<EntityModel<BouquetResponse>> getBouquets(
//            @Parameter(description = "Номер страницы (0..N)") @RequestParam(defaultValue = "0") int page,
//            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size
//    );

    @Operation(summary = "Создать новый букет")
    @ApiResponse(responseCode = "201", description = "Букет успешно создан")
    @ApiResponse(responseCode = "400", description = "Невалидный запрос", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @ApiResponse(responseCode = "409", description = "Букет с таким названием уже существует", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<BouquetResponse>> createBouquet(@Valid @RequestBody BouquetRequest request);

    @Operation(summary = "Обновить букет")
    @ApiResponse(responseCode = "200", description = "Букет обновлен")
    @ApiResponse(responseCode = "404", description = "Букет не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PutMapping("/{id}")
    EntityModel<BouquetResponse> updateBouquet(@PathVariable Long id, @Valid @RequestBody UpdateBouquetRequest request);

    @Operation(summary = "Обновить состав букета")
    @ApiResponse(responseCode = "200", description = "Состав букета обновлен")
    @ApiResponse(responseCode = "404", description = "Букет не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PatchMapping("/{id}/composition")
    EntityModel<BouquetResponse> updateBouquetComposition(@PathVariable Long id, @Valid @RequestBody List<BouquetFlowerItem> flowers);

    @Operation(summary = "Удалить букет")
    @ApiResponse(responseCode = "204", description = "Букет удален")
    @ApiResponse(responseCode = "404", description = "Букет не найден")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteBouquet(@PathVariable Long id);
}