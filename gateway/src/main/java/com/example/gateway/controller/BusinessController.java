package com.example.gateway.controller;

import com.example.gateway.client.OrderServiceClient;
import com.example.gateway.dto.OrderRequest;
import com.example.gateway.dto.TopUpRequest;
import com.example.gateway.service.OrderOrchestrationService;
import com.example.thrift.inventory.TInventoryService;
import com.example.thrift.inventory.TProduct;
import com.example.thrift.wallet.TWalletService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BusinessController {

    private final OrderOrchestrationService orderOrchestrationService;
    private final OrderServiceClient orderServiceClient;
    private final TWalletService.Client walletServiceClient;
    private final TInventoryService.Client inventoryServiceClient;

    public BusinessController(OrderOrchestrationService orderOrchestrationService,
                              OrderServiceClient orderServiceClient,
                              @Qualifier("walletServiceClient") TWalletService.Client walletServiceClient,
                              @Qualifier("inventoryServiceClient") TInventoryService.Client inventoryServiceClient) {
        this.orderOrchestrationService = orderOrchestrationService;
        this.orderServiceClient = orderServiceClient;
        this.walletServiceClient = walletServiceClient;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @PostMapping("/orders")
    public ResponseEntity<Map<String, String>> placeOrder(@Valid @RequestBody OrderRequest request,
                                                          @RequestParam(defaultValue = "false") boolean simulateFail) throws Exception {
        orderOrchestrationService.placeOrder(request, simulateFail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("message", "Order placed successfully"));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<?>> getOrders() {
        return ResponseEntity.ok(orderServiceClient.getAllOrders());
    }

    @PostMapping("/wallets/top-up")
    public ResponseEntity<Map<String, String>> topUpWallet(@Valid @RequestBody TopUpRequest request) throws Exception {
        walletServiceClient.topUp(request.getUserId(), request.getAmount());
        return ResponseEntity.ok(Collections.singletonMap("message", "Wallet topped up successfully"));
    }

    @GetMapping("/products")
    public ResponseEntity<List<TProduct>> getProducts() throws Exception {
        return ResponseEntity.ok(inventoryServiceClient.listProducts());
    }
}
