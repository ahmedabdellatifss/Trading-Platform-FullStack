package com.roaa.treading.service;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.roaa.treading.entity.PaymentOrder;
import com.roaa.treading.entity.User;
import com.roaa.treading.enums.PaymentMethod;
import com.roaa.treading.enums.PaymentOrderStatus;
import com.roaa.treading.repository.PaymentOrderRepository;
import com.roaa.treading.response.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecretKey;

    @Override
    public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        return paymentOrderRepository.findById(id).orElseThrow(()-> new Exception("Payment order not found"));
    }

    @Override
    public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecretKey);
                Payment payment = razorpay.payments.fetch(paymentId);

                Integer amount = payment.get("amount");
                String status = payment.get("status");

                if(status.equals("captured")){
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    return true;
                }
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);
            return true;
        }
        return false;
    }

    @Override
    public PaymentResponse createRazorpayPaymentLink(User user, Long amount) throws RazorpayException {

        Long Amount = amount * 100;
        try {
            // Instantiate a Razorpay client with your key ID and secret
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecretKey);

            //Create a Json object with Payment link request parameters
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount",Amount);
            paymentLinkRequest.put("currency", "INR");

            // Create a Json object with the customer details
            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            // Create a JSON object with the notification settings
            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            // Set the reminder settings
            paymentLinkRequest.put("reminder_enable", true);

            // Set the callback URl and method
            paymentLinkRequest.put("callback_url", "http://localhost:5173/wallet");
            paymentLinkRequest.put("callback_method", "get");

            // Create the payment link using the paymentLink.create() method
            PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentResponse res = new PaymentResponse();
            res.setPayment_url(paymentLinkUrl);

            return res;
        }catch (RazorpayException e){
            System.out.println("Error creating payment link: " + e.getMessage());
            throw new RazorpayException("Error creating payment link: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/wallet?order_id="+orderId)
                .setCancelUrl("http://localhost:5173/payment/cancel")
                .addLineItem(SessionCreateParams
                        .LineItem
                        .builder()
                        .setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams
                                        .LineItem
                                        .PriceData
                                        .builder()
                                        .setCurrency("usd")
                                        .setUnitAmount(amount*100)
                                        .setProductData(SessionCreateParams
                                                .LineItem
                                                .PriceData
                                                .ProductData
                                                .builder()
                                                .setName("Top up wallet")
                                                .build()
                                        ).build()
                        ).build()
                ).build();
        Session session = Session.create(params);
        System.out.println("session _____" + session);

        PaymentResponse res = new PaymentResponse();
        res.setPayment_url(session.getUrl());

        return res;
    }
}
