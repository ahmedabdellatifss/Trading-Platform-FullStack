package com.roaa.treading.controller;

import com.roaa.treading.entity.User;
import com.roaa.treading.entity.Wallet;
import com.roaa.treading.entity.WalletTransaction;
import com.roaa.treading.service.TransactionService;
import com.roaa.treading.service.UserService;
import com.roaa.treading.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/api/transactions")
    public ResponseEntity<List<WalletTransaction>> getAllTransactions(
            @RequestHeader("Authorization") String jwt
    ) throws Exception{
        User user = userService.findUserByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);

        List<WalletTransaction> transactionList = transactionService.getTransactionsByWallet(wallet);

        return ResponseEntity.ok(transactionList);
    }
}
