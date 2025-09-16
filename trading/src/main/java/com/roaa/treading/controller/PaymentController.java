package com.roaa.treading.controller;

import com.razorpay.RazorpayException;
import com.roaa.treading.entity.PaymentOrder;
import com.roaa.treading.entity.User;
import com.roaa.treading.enums.PaymentMethod;
import com.roaa.treading.response.PaymentResponse;
import com.roaa.treading.service.PaymentService;
import com.roaa.treading.service.UserService;
import com.stripe.exception.StripeException;
import jdk.jshell.spi.ExecutionControl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PaymentController {

    private final UserService userService;

    private final PaymentService paymentService;

    @GetMapping("/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @PathVariable("paymentMethod") PaymentMethod paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt) throws Exception, RazorpayException , StripeException

    {
        User user = userService.findUserByJwt(jwt);

        PaymentResponse paymentResponse;
        PaymentOrder order = paymentService.createOrder(user, amount, paymentMethod);

        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            paymentResponse = paymentService.createRazorpayPaymentLink(user, amount);
        }else {
            paymentResponse = paymentService.createStripePaymentLink(user, amount, order.getId());
        }
        return ResponseEntity.ok(paymentResponse);
    }
}
