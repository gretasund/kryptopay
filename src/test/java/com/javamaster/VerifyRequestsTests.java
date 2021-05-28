package com.javamaster;

import com.javamaster.controller.dto.PaymentDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class VerifyRequestsTests {

    @Test
    void verifyRequest() {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setMerchantTransactionId("1");
        paymentDTO.setMerchantId("1");
        paymentDTO.setCurrency("BTC");
        paymentDTO.setRecipientAddress("mgdnotTNCoEQTXghNeDT4HBgUk792AzJ3f");
        paymentDTO.setAmount(1L);
        paymentDTO.setReference("Test");
        Assert.assertTrue(paymentDTO.verifyPayment());
    }

    @Test
    void doNotVerifyRequest1() {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setMerchantTransactionId("1");
        paymentDTO.setMerchantId("1");
        paymentDTO.setCurrency("BTC");
        paymentDTO.setRecipientAddress("mgdnotTNCoEQTXghNeDT4HBgUk792AzJ3f");
        paymentDTO.setReference("Test");
        Assert.assertFalse(paymentDTO.verifyPayment());
    }

    @Test
    void doNotVerifyRequest2() {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setMerchantTransactionId("1");
        paymentDTO.setCurrency("BTC");
        paymentDTO.setRecipientAddress("mgdnotTNCoEQTXghNeDT4HBgUk792AzJ3f");
        paymentDTO.setAmount(1L);
        paymentDTO.setReference("Test");
        Assert.assertFalse(paymentDTO.verifyPayment());
    }

    @Test
    void doNotVerifyRequest3() {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setMerchantTransactionId("1");
        paymentDTO.setCurrency("NDG");
        paymentDTO.setRecipientAddress("mgdnotTNCoEQTXghNeDT4HBgUk792AzJ3f");
        paymentDTO.setAmount(1L);
        paymentDTO.setReference("Test");
        Assert.assertFalse(paymentDTO.verifyPayment());
    }

}