package com.roaa.treading.controller;

import com.roaa.treading.entity.Coin;
import com.roaa.treading.entity.Order;
import com.roaa.treading.entity.User;
import com.roaa.treading.enums.OrderType;
import com.roaa.treading.request.CreateOrderRequest;
import com.roaa.treading.service.CoinService;
import com.roaa.treading.service.OrderService;
import com.roaa.treading.service.UserService;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    private final UserService userService;

    private final CoinService coinService;

//    private final WalletTransactionService walletTransactionService;

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @RequestBody CreateOrderRequest req
    )throws Exception{
        User user = userService.findUserByJwt(jwt);
        Coin coin = coinService.findById(req.getCoinId());
        Order order = orderService.processOrder(coin, req.getQuantity(), req.getOrderType(), user);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId
    ) throws Exception{
        User user = userService.findUserByJwt(jwt);
        Order order = orderService.getOrderById(orderId);
        if(order.getUser().getId().equals(user.getId())){
            return ResponseEntity.ok(order);
        }else{
            throw new Exception("You don't have access");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) OrderType order_type,
            @RequestParam(required = false) String asset_symbol
    )throws Exception{
        Long userId = userService.findUserByJwt(jwt).getId();
        List<Order> userOrders = orderService.getAllOrderOfUser(userId,order_type, asset_symbol);
        return ResponseEntity.ok(userOrders);
    }



}
