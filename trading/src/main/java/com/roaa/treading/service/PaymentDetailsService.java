package com.roaa.treading.service;

import com.roaa.treading.entity.PaymentDetails;
import com.roaa.treading.entity.User;

public interface PaymentDetailsService {

    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountHolderName,
                                            String ifsc,
                                            String bankName,
                                            User user);

    public PaymentDetails getUsersPaymentDetails(User user);
}
