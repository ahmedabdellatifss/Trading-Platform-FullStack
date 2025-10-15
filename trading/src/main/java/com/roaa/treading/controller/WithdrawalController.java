package com.roaa.treading.controller;

import com.roaa.treading.entity.User;
import com.roaa.treading.entity.Wallet;
import com.roaa.treading.entity.WalletTransaction;
import com.roaa.treading.entity.Withdrawal;
import com.roaa.treading.enums.WalletTransactionType;
import com.roaa.treading.service.TransactionService;
import com.roaa.treading.service.UserService;
import com.roaa.treading.service.WalletService;
import com.roaa.treading.service.WithdrawalService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
public class WithdrawalController {

    private final WalletService walletService;

    private final UserService userService;

    private final WithdrawalService withdrawalService;

    private final TransactionService walletTransactionService;

    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<?> withdrawalRequest(
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt
    )throws Exception{
        User user = userService.findUserByJwt(jwt);
        Wallet userWallet = walletService.getUserWallet(user);
        Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount, user);
        walletService.addBalance(userWallet, -withdrawal.getAmount());

        walletTransactionService.createWalletTransaction(
                userWallet,
                WalletTransactionType.WITHDRAWAL,
                LocalDate.now(),
                "102",
                "bank account withdrawal",
                withdrawal.getAmount()
        );

        return ResponseEntity.ok(withdrawal);

    }

    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawalRequest(
            @PathVariable Long id,
            @PathVariable boolean accept,
            @RequestHeader("Authorization") String jwt
    ) throws Exception{
        User user = userService.findUserByJwt(jwt);
        Wallet userWallet = walletService.getUserWallet(user);
        Withdrawal withdrawal = withdrawalService.procedWithdrawal(id, accept);
        if(!accept){
            walletService.addBalance(userWallet, withdrawal.getAmount());
        }
        return ResponseEntity.ok(withdrawal);
    }

    @GetMapping("/api/withdrawal")
    public ResponseEntity<List<Withdrawal>> getWithdrawalHistory(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwt(jwt);
        List<Withdrawal> withdrawals = withdrawalService.getUserWithdrawalsHistory(user);
        return ResponseEntity.ok(withdrawals);
    }

    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>> getAllWithdrawalResquest(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwt(jwt);
        List<Withdrawal> withdrawals = withdrawalService.getAllWithdrawalRequests();
        return ResponseEntity.ok(withdrawals);
    }

}
