package com.roaa.treading.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roaa.treading.config.PaymobConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymobService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymobConfig config;

    public PaymobService(PaymobConfig config) {
        this.config = config;
    }

//    // Step 1: Get Auth Token
//    public String getAuthToken() throws Exception {
//        String url = config.getBaseUrl() + "/auth/tokens";
//
//        Map<String, String> request = new HashMap<>();
//        request.put("api_key", config.getApiKey());
//
//        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//        JsonNode node = objectMapper.readTree(response.getBody());
//
//        return node.get("token").asText();
//    }

    // Step 2: Create Order
//    public Long createOrder(String token, int amountCents, String currency, String orderId) throws Exception {
//        String url = config.getBaseUrl() + "/ecommerce/orders";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        Map<String, Object> request = new HashMap<>();
//        request.put("amount_cents", amountCents);
//        request.put("currency", currency);
//        request.put("merchant_order_id", orderId);
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//        JsonNode node = objectMapper.readTree(response.getBody());
//
//        return node.get("id").asLong();
//    }

//    // Step 3: Get Payment Key
//    public String getPaymentKey(String token, Long orderId, int amountCents, String currency) throws Exception {
//        String url = config.getBaseUrl() + "/acceptance/payment_keys";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        Map<String, Object> billingData = new HashMap<>();
//        billingData.put("first_name", "Ahmed");
//        billingData.put("last_name", "Mohamed");
//        billingData.put("email", "ahmed@example.com");
//        billingData.put("phone_number", "+201000000000");
//        billingData.put("street", "123 Main Street");
//        billingData.put("building", "13");
//        billingData.put("floor", "5");
//        billingData.put("apartment", "4");
//        billingData.put("city", "Cairo");
//        billingData.put("country", "EG");
//
//        Map<String, Object> request = new HashMap<>();
//        request.put("amount_cents", amountCents);
//        request.put("currency", currency);
//        request.put("order_id", orderId);
//        request.put("integration_id", config.getIntegrationId());
//        request.put("billing_data", billingData);
//        request.put("redirect_url", "http://localhost:5173/wallet");
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//        JsonNode node = objectMapper.readTree(response.getBody());
//
//        return node.get("token").asText();
//    }

//    // Step 4: Generate Payment URL (iframe)
//    public String getPaymentUrl(String paymentKey) {
//        return "https://accept.paymob.com/api/acceptance/iframes/" + config.getIframeId() + "?payment_token=" + paymentKey;
//    }
}
