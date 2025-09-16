package com.roaa.treading.controller;

import com.roaa.treading.entity.*;
import com.roaa.treading.response.PaymentResponse;
import com.roaa.treading.service.OrderService;
import com.roaa.treading.service.PaymentService;
import com.roaa.treading.service.UserService;
import com.roaa.treading.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction req
    ) throws Exception {
        User senderUser = userService.findUserByJwt(jwt);
        Wallet receiverWallet = walletService.findWalletById(walletId);
        Wallet wallet = walletService.walletToWalletTransfer(senderUser, receiverWallet, req.getAmount());
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId
    ) throws Exception {
        User user = userService.findUserByJwt(jwt);

        Order order = orderService.getOrderById(orderId);

        Wallet wallet = walletService.payOrderPayment(order, user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);

    }

    @PutMapping("/deposit")
    public ResponseEntity<Wallet> addBalanceToWallet(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(name = "order_id") Long orderId,
            @RequestParam(name = "payment_id") String paymentId

    ) throws Exception {
        User user = userService.findUserByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);

        PaymentOrder order = paymentService.getPaymentOrderById(orderId);

        Boolean status = paymentService.ProceedPaymentOrder(order, paymentId);

        if(status){
            wallet = walletService.addBalance(wallet, order.getAmount());
        }

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);

    }
}
