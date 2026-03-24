package com.example.gateway.service;

import com.example.gateway.client.OrderServiceClient;
import com.example.gateway.dto.OrderRequest;
import com.example.thrift.inventory.TInventoryService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OrderOrchestrationService {

    private final OrderServiceClient orderServiceClient;
    private final TInventoryService.Client inventoryServiceClient;

    public OrderOrchestrationService(OrderServiceClient orderServiceClient,
                                     @Qualifier("inventoryServiceClient") TInventoryService.Client inventoryServiceClient) {
        this.orderServiceClient = orderServiceClient;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @GlobalTransactional(name = "place-order")
    public void placeOrder(OrderRequest request, boolean simulateFail) throws TException {
        orderServiceClient.createOrder(
                request.getUserId(),
                request.getProductId(),
                request.getQuantity(),
                request.getTotalAmount());

        inventoryServiceClient.decrementStock(request.getProductId(), request.getQuantity());

        if (simulateFail) {
            throw new RuntimeException("Simulated failure — Seata will rollback all services");
        }
    }
}
