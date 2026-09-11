package com.sridharnagula.paymentservice.service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sridharnagula.paymentservice.dto.PaymentResponse;
import com.sridharnagula.paymentservice.exceptions.PaymentGatewayException;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("razorpay")
public class RazorpayPaymentServiceImpl implements PaymentService {

    private final RazorpayClient razorpayClient;
    private final Tracer tracer;

    @Value("${app.callback-base-url:http://localhost:8083}")
    private String callbackBaseUrl;

    public RazorpayPaymentServiceImpl(RazorpayClient razorpayClient, Tracer tracer) {
        this.razorpayClient = razorpayClient;
        this.tracer = tracer;
    }

    @Override
    public PaymentResponse doPayment(String email, String phone, Long amount, String orderId) throws PaymentGatewayException {
        Span span = tracer.spanBuilder("payment-service.razorpay-charge")
                .setAttribute("payment.gateway", "razorpay")
                .setAttribute("order.id", orderId)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount); // amount is already in currency subunits (paise)
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("receipt", orderId);

            JSONObject customer = new JSONObject();
            customer.put("email", email); // Razorpay's own API field - "email", not "emial"
            customer.put("contact", phone);
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("callback_url", callbackBaseUrl + "/razorpayWebHook");
            paymentLinkRequest.put("callback_method", "get");

            PaymentLink link = razorpayClient.paymentLink.create(paymentLinkRequest);

            PaymentResponse response = new PaymentResponse();
            response.setGateway("razorpay");
            response.setPaymentReferenceId(link.get("id"));
            response.setPaymentUrl(link.get("short_url"));
            response.setStatus(link.get("status"));
            response.setOrderId(orderId);
            response.setAmount(amount);

            span.setAttribute("payment.reference_id", response.getPaymentReferenceId());
            span.setStatus(StatusCode.OK);
            return response;
        } catch (RazorpayException e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, e.getMessage());
            throw new PaymentGatewayException("Razorpay payment link creation failed for order " + orderId, e);
        } finally {
            span.end();
        }
    }
}
