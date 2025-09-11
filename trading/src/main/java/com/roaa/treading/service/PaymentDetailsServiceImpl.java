package com.roaa.treading.service;

import com.roaa.treading.entity.PaymentDetails;
import com.roaa.treading.entity.User;
import com.roaa.treading.repository.PaymentDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentDetailsServiceImpl implements PaymentDetailsService{

    private final PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifsc, String bankName, User user) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setAccountHolderName(accountHolderName);
        paymentDetails.setIfsc(ifsc);
        paymentDetails.setBankName(bankName);
        paymentDetails.setUser(user);

        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUsersPaymentDetails(User user) {

        return paymentDetailsRepository.findByUserId(user.getId());
    }
}
