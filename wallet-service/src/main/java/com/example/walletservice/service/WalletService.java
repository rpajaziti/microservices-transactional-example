package com.example.walletservice.service;

import com.example.common.exception.InsufficientBalanceException;
import com.example.common.exception.ResourceNotFoundException;
import com.example.walletservice.entity.Wallet;
import com.example.walletservice.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void deductBalance(String userId, int amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + userId));

        if (wallet.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance for user: " + userId);
        }

        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.saveAndFlush(wallet);
    }

    public void topUp(String userId, int amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Wallet w = new Wallet();
                    w.setUserId(userId);
                    w.setBalance(0);
                    return w;
                });

        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.saveAndFlush(wallet);
    }
}
