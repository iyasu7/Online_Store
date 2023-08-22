package com.iyex.order.service;

import com.iyex.order.domain.OrderLineItems;
import com.iyex.order.domain.Orders;
import com.iyex.order.dto.InventoryDto;
import com.iyex.order.dto.OrderLineItemsDto;
import com.iyex.order.dto.OrderRequest;
import com.iyex.order.event.OrderPlacedEvent;
import com.iyex.order.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepo orderRepo;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    public String placeOrder(OrderRequest orderRequest){
        Orders order = new Orders();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtos()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItems(orderLineItems);
        //check inventory
        List<String> skuCodes = order
                .getOrderLineItems()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        InventoryDto[] inventoryDtos = webClientBuilder.build().get()
                .uri("http://inventory/api/v1/inventories",
                        uriBuilder ->
                                uriBuilder.queryParam("skuCode",skuCodes)
                                        .build())
                        .retrieve()
                                .bodyToMono(InventoryDto[].class)
                                .block();

        assert inventoryDtos != null;
//        Arrays.stream(inventoryDtos)
//                .iterator().
//                .toList();

        log.info(String.valueOf(inventoryDtos.length));

        boolean allInStock = Arrays.stream(inventoryDtos).allMatch(InventoryDto::isInStock);
        log.info("all in stock :"+allInStock);
        if (allInStock){
        orderRepo.save(order);
        kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
        return "order made successfully";
        }else {
            log.info("exc Hi");
            //TODO To return a list of Items that are out of stock;
//            final Stream<InventoryDto> outOfStockItems = Arrays.stream(inventoryDtos).filter(inventoryDto -> !inventoryDto.isInStock());
//            String outOfStock = "";
//            throw new IllegalArgumentException("Product is Out of stock", outOfStockItems.map(outOfStock -> outOfStock.));
            throw new IllegalArgumentException("Product is Out of stock");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
