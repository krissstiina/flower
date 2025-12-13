package grpc.demo.rabbitmq;

import grpc.demo.websocket.NotificationHandler;
import org.example.events.BouquetRatedEvent;
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
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    private final NotificationHandler handler;

    public NotificationListener(NotificationHandler handler) {
        this.handler = handler;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q.notifications.browser", durable = "true"),
            exchange = @Exchange(name = "analytics-fanout", type = "fanout")
    ))
    public void handleBouquetRated(BouquetRatedEvent event) {
        log.info("BouquetRatedEvent received: {}", event);

        String json = String.format("""
                {"type":"BOUQUET_RATED",
                 "bouquetId":%d,
                 "popularityScore":%d,
                 "popularityLevel":"%s",
                 "recommendation":"%s"}""",
                event.bouquetId(),
                event.popularityScore(),
                event.popularityLevel(),
                event.recommendation()
        );

        handler.broadcast(json);
    }

}

