package com.example.demo.fanout;

import org.example.events.BouquetRatedEvent;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class BouquetAnalyticsListener {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.demorest.bouquet.analytics.log", durable = "true"),
                    exchange = @Exchange(name = "analytics-fanout", type = "fanout")
            )
    )
    public void logBouquetRating(BouquetRatedEvent event) {
        System.out.println("Bouquet " + event.bouquetId() + " rated with popularity score " + event.popularityScore());
    }
}
