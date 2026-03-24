package com.example.gateway.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

public interface OrderServiceClient {

    @PostExchange("/orders")
    void createOrder(@RequestParam String userId,
                     @RequestParam Long productId,
                     @RequestParam Integer quantity,
                     @RequestParam Integer totalAmount);

    @GetExchange("/orders")
    List<?> getAllOrders();
}
