package com.javamaster.model;

import com.javamaster.controller.dto.PaymentDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bitcoinj.core.Sha256Hash;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class BitcoinPayment extends Payment {

    private Sha256Hash txId;
    private int depth;


    // constructor
    public BitcoinPayment (PaymentDTO paymentDTO) {
        BitcoinPayment.super.setCurrency(Currency.BTC);
        BitcoinPayment.super.setStatus(Status.INITIATED);

        BitcoinPayment.super.setMerchantId(paymentDTO.getMerchantId());

        BitcoinPayment.super.setMerchantTransactionId(paymentDTO.getMerchantTransactionId());
        BitcoinPayment.super.setRecipientAddress(paymentDTO.getRecipientAddress());
        BitcoinPayment.super.setAmount(paymentDTO.getAmount());
        BitcoinPayment.super.setReference(paymentDTO.getReference());
        BitcoinPayment.super.setAdditionalInformation(paymentDTO.getAdditionalInformation());
        BitcoinPayment.super.setOpeningTime(LocalDateTime.now());
        BitcoinPayment.super.setLogEntries(new ArrayList<>());
    }

}
