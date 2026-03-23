package com.example.orderservice.service;

import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.thrift.wallet.TWalletService;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final TWalletService.Client walletServiceClient;

    public OrderService(OrderRepository orderRepository,
                        @Qualifier("walletServiceClient") TWalletService.Client walletServiceClient) {
        this.orderRepository = orderRepository;
        this.walletServiceClient = walletServiceClient;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void createOrder(String userId, Long productId, Integer quantity, Integer totalAmount) throws TException {
        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalAmount(totalAmount);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);

        walletServiceClient.deductBalance(userId, totalAmount);
    }
}
