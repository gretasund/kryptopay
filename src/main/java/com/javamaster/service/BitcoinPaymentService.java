package com.javamaster.service;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;
import com.javamaster.model.BitcoinPayment;
import com.javamaster.model.Payment;
import com.javamaster.repository.BitcoinPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BitcoinPaymentService {

    private final BitcoinPaymentRepository bitcoinPaymentRepository;
    private SimpMessagingTemplate simpleMessagingTemplate;
    private final static NetworkParameters params = TestNet3Params.get();
    private final String filePrefix = "kryptopay-testnet";
    WalletAppKit kit = new WalletAppKit(params, new File("."), filePrefix);

    public BitcoinPayment findById(Long id) {
        Optional<BitcoinPayment> project = bitcoinPaymentRepository.findById(id);
        return project.orElse(null);
    }

    public BitcoinPayment save(BitcoinPayment payment) {
        return bitcoinPaymentRepository.save(payment);
    }

    @Async
    public void watchPayment(BitcoinPayment payment) {
        // Starting bitcoinj, download the block headers
        kit.startAsync();
        kit.awaitRunning();

        // Add merchants targetAddress to the wallets watch list
        kit.wallet().addWatchedAddress(LegacyAddress.fromBase58(params, payment.getRecipientAddress()));

        // We want to know when we receive money.
        kit.wallet().addCoinsReceivedEventListener((wallet, tx, prevBalance, newBalance) -> {

            Coin value = tx.getValueSentToMe(wallet);
            System.out.println("Transaction for " + value + "registered.");

            Futures.addCallback(tx.getConfidence().getDepthFuture(6), new FutureCallback<>() {
                @Override
                public void onSuccess(TransactionConfidence result) {
                    System.out.println("Payment registered in the blockchain.");
                    simpleMessagingTemplate.convertAndSend("/topic/payment/" + payment.getId(), new Gson().toJson(this));
                    payment.setStatus(Payment.Status.COMPLETED);
                    save(payment);
                }
                @Override
                public void onFailure(Throwable throwable) {
                    System.out.println("Something went wrong. The transaction could not be confirmed.");
                    simpleMessagingTemplate.convertAndSend("/topic/payment/" + payment.getId(), new Gson().toJson(this));
                    payment.setStatus(Payment.Status.INACTIVE);
                    save(payment);
                    throw new RuntimeException(throwable);
                }
            }, MoreExecutors.directExecutor());
        });
    }

    public void trackPayment(BitcoinPayment payment) {
        kit.wallet().addWatchedAddress(LegacyAddress.fromBase58(params, payment.getRecipientAddress()));
        kit.wallet().addTransactionConfidenceEventListener((w, tx) -> {
            payment.setDepth(tx.getConfidence().getDepthInBlocks());
            save(payment);
            System.out.println("Confidence of " + tx.getTxId() + " has changed to " + tx.getConfidence().getDepthInBlocks());
            //simpleMessagingTemplate.convertAndSend("/topic/payment/" + payment.getId(), "The payment has reached a depth of " + tx.getConfidence().getDepthInBlocks());
            if(tx.getConfidence().getDepthInBlocks() >= 6) {
                payment.setStatus(Payment.Status.COMPLETED);
                save(payment);
            }
        });
    }


}