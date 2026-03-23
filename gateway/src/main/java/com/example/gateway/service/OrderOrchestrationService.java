package com.example.gateway.service;

import com.example.gateway.dto.OrderRequest;
import com.example.thrift.inventory.TInventoryService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OrderOrchestrationService {

    private final RestTemplate restTemplate;
    private final TInventoryService.Client inventoryServiceClient;

    @Value("${order-service.endpoint}")
    private String orderServiceEndpoint;

    public OrderOrchestrationService(RestTemplate restTemplate,
                                     @Qualifier("inventoryServiceClient") TInventoryService.Client inventoryServiceClient) {
        this.restTemplate = restTemplate;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @GlobalTransactional(name = "place-order")
    public void placeOrder(OrderRequest request, boolean simulateFail) throws TException {
        String url = UriComponentsBuilder.fromHttpUrl(orderServiceEndpoint)
                .queryParam("userId", request.getUserId())
                .queryParam("productId", request.getProductId())
                .queryParam("quantity", request.getQuantity())
                .queryParam("totalAmount", request.getTotalAmount())
                .toUriString();
        restTemplate.postForObject(url, null, Void.class);

        inventoryServiceClient.decrementStock(request.getProductId(), request.getQuantity());

        if (simulateFail) {
            throw new RuntimeException("Simulated failure — Seata will rollback all services");
        }
    }
}
