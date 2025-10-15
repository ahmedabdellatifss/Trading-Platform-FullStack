package com.roaa.treading.repository;

import com.roaa.treading.entity.Wallet;
import com.roaa.treading.entity.WalletTransaction;
import com.stripe.model.issuing.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<WalletTransaction, Long> {

    List<WalletTransaction> findByWallet(Wallet wallet);
}
