package com.roaa.treading.service;

import com.roaa.treading.entity.User;
import com.roaa.treading.entity.Withdrawal;

import java.util.List;

public interface WithdrawalService {

    Withdrawal requestWithdrawal(Long amount, User user);

    Withdrawal procedWithdrawal(Long withdrawalId, boolean accept) throws Exception;

    List<Withdrawal> getUserWithdrawalsHistory(User user);

    List<Withdrawal> getAllWithdrawalRequests();
}
