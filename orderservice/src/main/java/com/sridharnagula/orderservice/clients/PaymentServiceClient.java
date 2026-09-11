package com.sridharnagula.orderservice.clients;

import com.sridharnagula.orderservice.configs.ServiceUrlProperties;
import com.sridharnagula.orderservice.dtos.InitiatePaymentRequestDTO;
import com.sridharnagula.orderservice.dtos.RemoteUserDTO;
import com.sridharnagula.orderservice.exceptions.PaymentFailedException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentServiceClient {

    private final RestTemplate restTemplate;
    private final ServiceUrlProperties serviceUrlProperties;

    public PaymentServiceClient(RestTemplate restTemplate, ServiceUrlProperties serviceUrlProperties) {
        this.restTemplate = restTemplate;
        this.serviceUrlProperties = serviceUrlProperties;
    }

    public String initiatePayment(RemoteUserDTO user, long amountInSubunits, String orderId) throws PaymentFailedException {
        String url = serviceUrlProperties.getPaymentServiceUrl() + "/payment";
        InitiatePaymentRequestDTO request = new InitiatePaymentRequestDTO();
        request.setEmial(user.getEmail());
        request.setAmount(amountInSubunits);
        request.setOrderId(orderId);
        try {
            return restTemplate.postForObject(url, request, String.class);
        } catch (RestClientException e) {
            throw new PaymentFailedException("Payment initiation failed for order " + orderId + ": " + e.getMessage());
        }
    }
}
