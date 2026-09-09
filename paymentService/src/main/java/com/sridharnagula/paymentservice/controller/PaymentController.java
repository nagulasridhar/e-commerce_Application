package com.sridharnagula.paymentservice.controller;

import com.sridharnagula.paymentservice.dto.InitiatePaymentTRequestDTO;
import com.sridharnagula.paymentservice.dto.PaymentResponse;
import com.sridharnagula.paymentservice.exceptions.PaymentGatewayException;
import com.sridharnagula.paymentservice.service.PaymentService;
import com.sridharnagula.paymentservice.service.strategy.PaymentGatewaySelectionStrategy;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    //@Autowired It is called Field Injection - not required if you do Constructor Injection
    private final PaymentService razorpayPaymentService;
    private final PaymentService stripePaymentService;
    private final PaymentGatewaySelectionStrategy paymentGatewaySelectionStrategy;

    public PaymentController(@Qualifier("stripe") PaymentService stripePaymentService,
                             @Qualifier("razorpay") PaymentService razorpayPaymentService,
                             PaymentGatewaySelectionStrategy paymentGatewaySelectionStrategy) {
        this.stripePaymentService = stripePaymentService;
        this.razorpayPaymentService = razorpayPaymentService;
        this.paymentGatewaySelectionStrategy = paymentGatewaySelectionStrategy;
    }

//    This is called Setter Injection - not preferred as just only for injection as we have other methods
//    public void setPaymentService(PaymentService paymentService) {
//        this.paymentService = paymentService;
//    }

    @PostMapping("/payment")
    public PaymentResponse initiatePayment(@Valid @RequestBody InitiatePaymentTRequestDTO requestDto) throws PaymentGatewayException {
        PaymentService gateway = choosePaymentGateway() == 1 ? razorpayPaymentService : stripePaymentService;
        return gateway.doPayment(
                requestDto.getEmial(),
                requestDto.getPhoneNumber(),
                requestDto.getAmount(),
                requestDto.getOrderId());
    }

    private int choosePaymentGateway(){
        return paymentGatewaySelectionStrategy.paymentGatewaySelection();
    }

}
