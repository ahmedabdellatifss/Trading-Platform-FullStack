package com.roaa.treading.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.roaa.treading.config.PaymobConfig;
import com.roaa.treading.config.RazorpayConfig;
import com.roaa.treading.config.StripeConfig;
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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService{


    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    private final StripeConfig stripeConfig;
    private final RazorpayConfig razorpayConfig;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymobConfig config;

    public PaymentServiceImpl(StripeConfig stripeConfig, RazorpayConfig razorpayConfig, PaymobConfig paymobConfig) {
        this.stripeConfig = stripeConfig;
        this.razorpayConfig = razorpayConfig;
        this.config = paymobConfig;
    }

    // Step 1: Get Auth Token
    public String getAuthToken() throws Exception {
        String url = config.getBaseUrl() + "/auth/tokens";

        Map<String, String> request = new HashMap<>();
        request.put("api_key", config.getApiKey());

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        JsonNode node = objectMapper.readTree(response.getBody());

        return node.get("token").asText();
    }

    // Step 2: Create Order for PayMob gateway platform
    public Long createPayMobOrder(String token, int amountCents, String currency, String internalOrderId) throws Exception {
        String url = config.getBaseUrl() + "/ecommerce/orders";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> request = new HashMap<>();
        request.put("amount_cents", amountCents);
        request.put("currency", currency);
        request.put("merchant_order_id", internalOrderId);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        JsonNode node = objectMapper.readTree(response.getBody());

        return node.get("id").asLong();
    }

    // Step 3: Get Payment Key for payMob gateway platform
    public String getPaymentKey(String token, Long payMobOrderId, int amountCents, String currency,User user, String internalOrder_id) throws Exception {
        String url = config.getBaseUrl() + "/acceptance/payment_keys";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> billingData = new HashMap<>();
        billingData.put("first_name", user.getFullName());
        billingData.put("last_name", "Mohamed");
        billingData.put("email", user.getEmail());
        billingData.put("phone_number", user.getMobile());
        billingData.put("street", "123 Main Street");
        billingData.put("building", "13");
        billingData.put("floor", "5");
        billingData.put("apartment", "4");
        billingData.put("city", "Cairo");
        billingData.put("country", "EG");

        Map<String, Object> request = new HashMap<>();
        request.put("amount_cents", amountCents);
        request.put("currency", currency);
        request.put("order_id", payMobOrderId);
        request.put("integration_id", config.getIntegrationId());
        request.put("billing_data", billingData);
        request.put("redirect_url", "http://localhost:5173/wallet?internalOrder_id="+internalOrder_id);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        JsonNode node = objectMapper.readTree(response.getBody());

        return node.get("token").asText();
    }

    // Step 4: Generate Payment URL (iframe)
    public String getPaymentUrl(String paymentKey) {
        return "https://accept.paymob.com/api/acceptance/iframes/" + config.getIframeId() + "?payment_token=" + paymentKey;
    }


    @Override
    public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        paymentOrder.setPaymentMethod(paymentMethod);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        return paymentOrderRepository.findById(id).orElseThrow(()-> new Exception("Payment order not found"));
    }

    @Override
    public void linkPaymobOrder(Long internalOrderId, Long paymobOrderId) {
        PaymentOrder order = paymentOrderRepository.findById(internalOrderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setPaymobOrderId(paymobOrderId);
        order.setStatus(PaymentOrderStatus.PENDING);
        paymentOrderRepository.save(order);
    }

    @Override
    public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws Exception {

        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpay = new RazorpayClient(razorpayConfig.getKey(),razorpayConfig.getSecret());
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
            } else if (paymentOrder.getPaymentMethod().equals(PaymentMethod.PAYMOB)) {
                String url = "https://accept.paymob.com/api/ecommerce/orders/" + paymentOrder.getPaymobOrderId();
                String token = getAuthToken();
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(token); // auth token you got earlier
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Void> entity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    return true;
                } else {
                    paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                    paymentOrderRepository.save(paymentOrder);
                    return false;
                }

            }
            paymentOrder.setStatus(PaymentOrderStatus.FAILED);
            paymentOrderRepository.save(paymentOrder);
            return false;
        }
        return false;
    }

    @Override
    public PaymentResponse createRazorpayPaymentLink(User user, Long amount) throws RazorpayException {

        Long Amount = amount * 100;
        try {
            // Instantiate a Razorpay client with your key ID and secret
            RazorpayClient razorpayClient = new RazorpayClient(razorpayConfig.getKey(), razorpayConfig.getSecret());

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
        Stripe.apiKey = stripeConfig.getApiKey();

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
