package com.roaa.treading.service;

import com.roaa.treading.entity.Wallet;
import com.roaa.treading.entity.WalletTransaction;
import com.roaa.treading.enums.WalletTransactionType;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    WalletTransaction createWalletTransaction(Wallet wallet,
                                              WalletTransactionType type,
                                              LocalDate date,
                                              String transferId,
                                              String purpose,
                                              Long amount);
    List<WalletTransaction> getTransactionsByWallet(Wallet wallet);
}
