package com.roaa.treading.controller;

import com.roaa.treading.entity.PaymentDetails;
import com.roaa.treading.entity.User;
import com.roaa.treading.service.PaymentDetailsService;
import com.roaa.treading.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class PaymentDetailsController {

    private final PaymentDetailsService paymentDetailsService;
    private final UserService userService;

    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(
            @RequestBody PaymentDetails paymentDetailsReq,
            @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = userService.findUserByJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.addPaymentDetails(
                paymentDetailsReq.getAccountNumber(),
                paymentDetailsReq.getAccountHolderName(),
                paymentDetailsReq.getIfsc(),
                paymentDetailsReq.getBankName(),
                user
        );
        return ResponseEntity.ok(paymentDetails);

    }

    @GetMapping("/payment-details")
    public ResponseEntity<PaymentDetails> getUserPaymentDetails(
            @RequestHeader("Authorization") String jwt
    ) throws Exception{
        User user = userService.findUserByJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.getUsersPaymentDetails(user);
        return ResponseEntity.ok(paymentDetails);
    }


}
