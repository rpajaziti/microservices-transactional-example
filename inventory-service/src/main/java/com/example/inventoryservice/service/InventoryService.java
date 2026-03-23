package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.Product;
import com.example.inventoryservice.repository.ProductRepository;
import com.example.thrift.inventory.TProduct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void decrementStock(long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + productId);
        }

        product.setStock(product.getStock() - quantity);
        productRepository.saveAndFlush(product);
    }

    public List<TProduct> listProducts() {
        return productRepository.findAll().stream()
                .map(p -> new TProduct(p.getId(), p.getProductName(), p.getStock()))
                .collect(Collectors.toList());
    }
}
