package com.javamaster.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Entity
@Data
public abstract class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String merchantId;
    private String merchantTransactionId;
    private Enum<Currency> currency;
    private String recipientAddress;
    private Long amount;
    private String reference;
    private String additionalInformation;
    private Enum<Status> status;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,
                mappedBy = "payment")
    @OrderBy
    protected List<LogEntry> logEntries;


    // enums
    public enum Status {
        INITIATED,
        PENDING,
        COMPLETED,
        INACTIVE;
    }

    public enum Currency {
        BTC;

        public static Enum<Currency> getCurrencyEnum(String str) {
            for (Enum<Currency> currencyEnum : Currency.values()) {
                if (currencyEnum.name().equalsIgnoreCase(str))
                    return currencyEnum;
            }
            return null;
        }

    }

}
