package com.example.inventoryservice.controller;

import com.example.inventoryservice.service.InventoryService;
import com.example.thrift.inventory.TInventoryService;
import com.example.thrift.inventory.TProduct;
import org.apache.thrift.TException;

import java.util.List;

public class InventoryThriftController implements TInventoryService.Iface {

    private final InventoryService inventoryService;

    public InventoryThriftController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    public void decrementStock(long productId, int quantity) throws TException {
        try {
            inventoryService.decrementStock(productId, quantity);
        } catch (RuntimeException e) {
            throw new TException(e.getMessage(), e);
        }
    }

    @Override
    public List<TProduct> listProducts() throws TException {
        try {
            return inventoryService.listProducts();
        } catch (RuntimeException e) {
            throw new TException(e.getMessage(), e);
        }
    }
}
