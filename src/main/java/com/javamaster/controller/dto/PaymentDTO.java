package com.javamaster.controller.dto;

import com.javamaster.model.Payment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;


@Getter
@Setter
@RequiredArgsConstructor
public class PaymentDTO {

    // TODO check type of merchantId and merchantTransactionId
    private String merchantId;
    private String merchantTransactionId;
    private String currency;
    private String recipientAddress;
    private Long amount;
    private String reference;
    private String additionalInformation;


    public boolean verifyPayment() {
        // validating the formal correctness of the request
        boolean validFormalities = StringUtils.isNoneEmpty(
                this.merchantId, this.merchantTransactionId,
                this.currency, this.recipientAddress, this.reference);

        // check if the currency is supported
        Enum<Payment.Currency> currencyEnum =
                Payment.Currency.getCurrencyEnum(this.currency);

        return validFormalities && (currencyEnum != null) && (amount != null);
    }

}
