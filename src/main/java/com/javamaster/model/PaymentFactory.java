package com.javamaster.model;

import com.javamaster.controller.dto.PaymentDTO;
import org.springframework.stereotype.Component;


@Component
public class PaymentFactory {

    public Payment createPayment(PaymentDTO paymentDTO){
        Payment payment = null;

        if(paymentDTO.getCurrency().equals("BTC")) {
            payment = new BitcoinPayment(paymentDTO);
        }

        /*
        possibility to extendable by
        elseif(currency.equals("ETH")) {
            payment = new EtherPayment();
        }
        */

        return payment;
    }

    // othter methods

}
