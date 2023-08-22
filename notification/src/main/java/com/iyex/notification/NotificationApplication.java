package com.iyex.notification;

import com.iyex.notification.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

//    @KafkaL
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        log.info("Received notification for order - {}", orderPlacedEvent.getOrderNumber());
    }
}
