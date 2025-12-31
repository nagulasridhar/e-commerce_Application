package com.sridharnagula.paymentservice.service.strategy;

import org.springframework.stereotype.Service;

@Service
public class PaymentGatewaySelectionStrategyImpl implements PaymentGatewaySelectionStrategy{

    @Override
    public int paymentGatewaySelection() {
        int x = (int) (Math.random()*10); //50 50
        if(x<5) {
            return 1;        }
        return 2;
    }
}
