package com.example.demo.listeners;

import org.example.events.FlowerStockLowEvent;
import org.example.events.OrderDeliveredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FlowerEventListener {

    private static final Logger log = LoggerFactory.getLogger(FlowerEventListener.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "inventory-alerts-queue", durable = "true"),
            exchange = @Exchange(name = "flowers-exchange", type = "topic"),
            key = "flower.stock.low"
    ))
    public void handleFlowerStockLowEvent(FlowerStockLowEvent event) {
        log.warn("ВНИМАНИЕ: Низкий запас цветов!");
        log.warn("Детали: Цветок '{}' (цвет: {})", event.flowerName(), event.flowerColor());
        log.warn("Остаток: {} шт. (минимальный порог: {} шт.)",
                event.currentStock(), event.minimumStockThreshold());
        log.warn("Рекомендуемое действие: Пополнить запасы цветка ID: {}", event.flowerId());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "order-delivery-queue", durable = "true"),
            exchange = @Exchange(name = "flowers-exchange", type = "topic"),
            key = "order.delivered"
    ))
    public void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        log.info("ЗАКАЗ ДОСТАВЛЕН!");
        log.info("Номер заказа: {}", event.orderId());
        log.info("Букет: '{}' (ID: {})", event.bouquetName(), event.bouquetId());
        log.info("Клиент: {}", event.customerInfo());
        log.info("Адрес доставки: {}", event.deliveryAddress());
        log.info("Количество: {} шт.", event.quantity());
        log.info("Сумма заказа: {} руб.", event.totalPrice());
        log.info("Дата заказа: {}", event.orderDate());
        log.info("Дата доставки: {}", event.deliveredAt());

    }

}