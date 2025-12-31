package com.sridharnagula.paymentservice.controller;

import com.razorpay.RazorpayException;
import com.sridharnagula.paymentservice.dto.InitiatePaymentTRequestDTO;
import com.sridharnagula.paymentservice.service.strategy.PaymentGatewaySelectionStrategy;
import com.sridharnagula.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    //@Autowired It is called Field Injection - not required if you do Constructor Injection
    private PaymentService razorpayPaymentService;
    private PaymentService stripePaymentService;
    private PaymentGatewaySelectionStrategy paymentGatewaySelectionStrategy;

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
    public String initiatePayment(@RequestBody InitiatePaymentTRequestDTO requestDto) throws RazorpayException {
        int paymentGatewayOption = choosePaymentGateway();
        switch (paymentGatewayOption){
            case 1 : return razorpayPaymentService.doPayment(
                    requestDto.getEmial(),
                    requestDto.getPhoneNumber(),
                    requestDto.getAmount(),
                    requestDto.getOrderId());
            case 2 : return stripePaymentService.doPayment(
                    requestDto.getEmial(),
                    requestDto.getPhoneNumber(),
                    requestDto.getAmount(),
                    requestDto.getOrderId());
        }
        return null;
    }
    private int choosePaymentGateway(){
        return paymentGatewaySelectionStrategy.paymentGatewaySelection();
    }

}
