package com.sridharnagula.paymentservice.controller;

import com.razorpay.RazorpayException;
import com.razorpay.Utils;
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


@RestController
@RequestMapping("/razorpayWebHook")
public class RazorpayWebHookController {

    private static final Logger logger = LoggerFactory.getLogger(RazorpayWebHookController.class);

    private final Tracer tracer;

    @Value("${razorpay.webhook.secret:}")
    private String webhookSecret;

    public RazorpayWebHookController(Tracer tracer) {
        this.tracer = tracer;
    }

    @PostMapping
    public ResponseEntity<String> acceptWebHookRequest(@RequestBody String payload,
                                                       @RequestHeader("X-Razorpay-Signature") String signature) {
        Span span = tracer.spanBuilder("payment-service.razorpay-webhook").startSpan();
        try (Scope scope = span.makeCurrent()) {
            Utils.verifyWebhookSignature(payload, signature, webhookSecret);
            logger.info("Verified Razorpay webhook received: {}", payload);
            span.setStatus(StatusCode.OK);
            return ResponseEntity.ok("ok");
        } catch (RazorpayException e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, "signature verification failed");
            logger.warn("Rejected Razorpay webhook - signature verification failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("invalid signature");
        } finally {
            span.end();
        }
    }
}
