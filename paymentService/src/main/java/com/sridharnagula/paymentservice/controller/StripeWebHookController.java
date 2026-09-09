package com.sridharnagula.paymentservice.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Previously had no endpoint at all - Stripe would have gotten a 404 on every webhook
 *  attempt and, after enough failures, disabled the endpoint in the dashboard. */
@RestController
@RequestMapping("/stripeWebHook")
public class StripeWebHookController {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebHookController.class);

    private final Tracer tracer;

    @Value("${stripe.webhook.secret:}")
    private String webhookSecret;

    public StripeWebHookController(Tracer tracer) {
        this.tracer = tracer;
    }

    @PostMapping
    public ResponseEntity<String> acceptWebHookRequest(@RequestBody String payload,
                                                       @RequestHeader("Stripe-Signature") String sigHeader) {
        Span span = tracer.spanBuilder("payment-service.stripe-webhook").startSpan();
        try (Scope scope = span.makeCurrent()) {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            span.setAttribute("stripe.event_type", event.getType());
            logger.info("Verified Stripe webhook received: type={} id={}", event.getType(), event.getId());
            span.setStatus(StatusCode.OK);
            return ResponseEntity.ok("ok");
        } catch (SignatureVerificationException e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, "signature verification failed");
            logger.warn("Rejected Stripe webhook - signature verification failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("invalid signature");
        } finally {
            span.end();
        }
    }
}
