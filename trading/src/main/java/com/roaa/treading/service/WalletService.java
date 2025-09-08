package com.roaa.treading.service;

import com.roaa.treading.entity.Order;
import com.roaa.treading.entity.User;
import com.roaa.treading.entity.Wallet;

public interface WalletService {

    Wallet getUserWallet(User user);

    Wallet addBalance(Wallet wallet, Long money);

    Wallet findWalletById(Long id) throws Exception;

    Wallet walletToWalletTransfer(User user, Wallet receiverWallet, Long amount) throws Exception;

    Wallet payOrderPayment(Order order, User user) throws Exception;

}
