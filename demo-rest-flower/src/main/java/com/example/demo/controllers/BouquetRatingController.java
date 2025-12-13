package com.example.demo.controllers;

import com.example.demo.config.RabbitMQConfig;
import grpc.flowers.BouquetAnalyticsRequest;
import grpc.flowers.BouquetAnalyticsServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.events.BouquetRatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BouquetRatingController {

    @GrpcClient("analytics-service")
    private BouquetAnalyticsServiceGrpc.BouquetAnalyticsServiceBlockingStub analyticsStub;

    private final RabbitTemplate rabbitTemplate;

    public BouquetRatingController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/api/bouquets/{id}/rate")
    public String rateBouquet(@PathVariable Long id) {
        // Вызов gRPC сервиса
        var request = BouquetAnalyticsRequest.newBuilder()
                .setBouquetId(id)
                .setTotalSales(500)      // пример данных
                .setRecentSales(50)      // пример данных
                .setUserLikes(120)       // пример данных
                .build();

        var gRpcResponse = analyticsStub.calculateBouquetPopularity(request);

        // Создаем событие для Fanout
        var event = new BouquetRatedEvent(
                gRpcResponse.getBouquetId(),
                gRpcResponse.getPopularityScore(),
                gRpcResponse.getPopularityLevel(),
                gRpcResponse.getRecommendation()
        );

        // Отправка в RabbitMQ Fanout (routingKey пустой)
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", event);

        return "Bouquet popularity calculated: " + gRpcResponse.getPopularityScore();
    }
}
