package com.example.gateway.service;

import com.example.gateway.dto.OrderRequest;
import com.example.thrift.inventory.TInventoryService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OrderOrchestrationService {

    private final RestClient restClient;
    private final TInventoryService.Client inventoryServiceClient;

    @Value("${order-service.endpoint}")
    private String orderServiceEndpoint;

    public OrderOrchestrationService(RestClient restClient,
                                     @Qualifier("inventoryServiceClient") TInventoryService.Client inventoryServiceClient) {
        this.restClient = restClient;
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
        restClient.post().uri(url).retrieve().toBodilessEntity();

        inventoryServiceClient.decrementStock(request.getProductId(), request.getQuantity());

        if (simulateFail) {
            throw new RuntimeException("Simulated failure — Seata will rollback all services");
        }
    }
}
