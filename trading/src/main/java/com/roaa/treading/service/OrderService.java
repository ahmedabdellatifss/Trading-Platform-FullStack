package com.roaa.treading.service;

import com.roaa.treading.entity.Coin;
import com.roaa.treading.entity.Order;
import com.roaa.treading.entity.OrderItem;
import com.roaa.treading.entity.User;
import com.roaa.treading.enums.OrderType;

import java.util.List;

public interface OrderService {

    Order createOrder(User user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long orderId) throws Exception;

    List<Order> getAllOrderOfUser(Long userId, OrderType orderType, String assetSymbol);

    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;
}
