package com.example.gateway.service;

import com.example.gateway.client.OrderServiceClient;
import com.example.gateway.dto.OrderRequest;
import com.example.thrift.inventory.TInventoryService;
import info.developerblog.spring.thrift.annotation.ThriftClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

@Service
public class OrderOrchestrationService {

    @ThriftClient(serviceId = "inventory-service")
    private TInventoryService.Client inventoryServiceClient;

    private final OrderServiceClient orderServiceClient;

    public OrderOrchestrationService(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
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
