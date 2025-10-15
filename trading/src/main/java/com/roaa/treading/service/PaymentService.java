package com.roaa.treading.service;

import com.razorpay.RazorpayException;
import com.roaa.treading.entity.PaymentOrder;
import com.roaa.treading.entity.User;
import com.roaa.treading.enums.PaymentMethod;
import com.roaa.treading.response.PaymentResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {

    PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod);
    PaymentOrder getPaymentOrderById(Long id) throws Exception;
    Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws Exception;
    PaymentResponse createRazorpayPaymentLink(User user, Long amount) throws RazorpayException;
    PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
    String getAuthToken()throws Exception;
    Long createPayMobOrder(String token, int amountCents, String currency, String internalOrderId) throws Exception;
    void linkPaymobOrder(Long internalOrderId, Long paymobOrderId);
    String getPaymentKey(String token, Long orderId, int amountCents, String currency,User user, String internalOrder_id) throws Exception;
    String getPaymentUrl(String paymentKey);

}


