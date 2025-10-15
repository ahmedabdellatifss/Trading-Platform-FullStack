package com.roaa.treading.service;

import com.roaa.treading.entity.Wallet;
import com.roaa.treading.entity.WalletTransaction;
import com.roaa.treading.enums.WalletTransactionType;
import com.roaa.treading.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public WalletTransaction createWalletTransaction( Wallet wallet,
                                                      WalletTransactionType type,
                                                      LocalDate date,
                                                      String transferId,
                                                      String purpose,
                                                       Long amount) {
        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setType(type);
        transaction.setDate(date);
        transaction.setTransferId(transferId);
        transaction.setPurpose(purpose);
        transaction.setAmount(amount);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<WalletTransaction> getTransactionsByWallet(Wallet wallet) {

        return transactionRepository.findByWallet(wallet);
    }
}
