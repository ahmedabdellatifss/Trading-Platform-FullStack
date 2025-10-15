package com.roaa.treading.controller;

import com.razorpay.RazorpayException;
import com.roaa.treading.entity.PaymentOrder;
import com.roaa.treading.entity.User;
import com.roaa.treading.enums.PaymentMethod;
import com.roaa.treading.response.PaymentResponse;
import com.roaa.treading.service.PaymentService;
import com.roaa.treading.service.UserService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

//    @Autowired
//    private PaymobService paymobService;

    @PostMapping("/payment/{paymentMethod}/amount/{amount}")
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

    // pay from PayMob gateway
    @GetMapping("/pay/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> pay(
            @RequestHeader("Authorization") String jwt,
            @PathVariable("paymentMethod") PaymentMethod paymentMethod,
            @PathVariable int amount
    ) throws Exception {
        User user = userService.findUserByJwt(jwt);
        PaymentOrder order = paymentService.createOrder(user, (long) amount, paymentMethod);

        String token = paymentService.getAuthToken();
        Long payMobOrderId = paymentService.createPayMobOrder(token, amount * 100, "EGP", String.valueOf(order.getId()));
        // 3. Link internal + external IDs
        paymentService.linkPaymobOrder(order.getId(), payMobOrderId);
        String paymentKey = paymentService.getPaymentKey(token, payMobOrderId, amount * 100, "EGP", user,String.valueOf(order.getId()));
        String paymentURL =  paymentService.getPaymentUrl(paymentKey);

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPayment_url(paymentURL);
        return ResponseEntity.ok(paymentResponse);
    }
}
