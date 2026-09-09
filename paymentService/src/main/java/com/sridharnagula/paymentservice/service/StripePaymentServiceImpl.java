package com.sridharnagula.paymentservice.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.param.PaymentLinkCreateParams;
import com.sridharnagula.paymentservice.dto.PaymentResponse;
import com.sridharnagula.paymentservice.exceptions.PaymentGatewayException;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("stripe")
public class StripePaymentServiceImpl implements PaymentService {

    private final Tracer tracer;

    public StripePaymentServiceImpl(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public PaymentResponse doPayment(String email, String phone, Long amount, String orderId) throws PaymentGatewayException {
        Span span = tracer.spanBuilder("payment-service.stripe-charge")
                .setAttribute("payment.gateway", "stripe")
                .setAttribute("order.id", orderId)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("order_id", orderId);
            metadata.put("customer_email", email);
            if (phone != null) {
                metadata.put("customer_phone", phone);
            }

            PaymentLinkCreateParams params = PaymentLinkCreateParams.builder()
                    .addLineItem(
                            PaymentLinkCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            PaymentLinkCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd") // amount is expected in the smallest unit (cents)
                                                    .setUnitAmount(amount)
                                                    .setProductData(
                                                            PaymentLinkCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Order " + orderId)
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .putAllMetadata(metadata)
                    .build();

            PaymentLink link = PaymentLink.create(params);

            PaymentResponse response = new PaymentResponse();
            response.setGateway("stripe");
            response.setPaymentReferenceId(link.getId());
            response.setPaymentUrl(link.getUrl());
            response.setStatus(link.getActive() != null && link.getActive() ? "active" : "inactive");
            response.setOrderId(orderId);
            response.setAmount(amount);

            span.setAttribute("payment.reference_id", response.getPaymentReferenceId());
            span.setStatus(StatusCode.OK);
            return response;
        } catch (StripeException e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, e.getMessage());
            throw new PaymentGatewayException("Stripe payment link creation failed for order " + orderId, e);
        } finally {
            span.end();
        }
    }
}
