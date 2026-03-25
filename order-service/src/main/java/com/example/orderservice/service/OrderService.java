package com.example.orderservice.service;

import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.thrift.wallet.TWalletService;
import info.developerblog.spring.thrift.annotation.ThriftClient;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @ThriftClient(serviceId = "wallet-service")
    private TWalletService.Client walletServiceClient;

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
