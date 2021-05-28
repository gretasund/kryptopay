package com.javamaster;

import org.bitcoinj.core.*;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
public class SendCoinsTest {

    private final static NetworkParameters params = TestNet3Params.get();
    private final String filePrefix = "kryptopay-testnet";
    private static WalletAppKit kit;


    @Test
    void getBalance() {
        // Set up wallet
        kit = new WalletAppKit(params, new File("."), filePrefix);
        kit.startAsync();
        kit.awaitRunning();

        // Create ECKey
        String privateKeyString = "cVrYUWwfMCVN1tLnarqfRE3kG3ipCv73Cvv8V5K4Sq92EHt9YPPs";
        DumpedPrivateKey privateKey = DumpedPrivateKey.fromBase58(TestNet3Params.get(), privateKeyString);
        ECKey ecKey = privateKey.getKey();

        // Add ECKey to wallet
        kit.wallet().importKey(ecKey);
        Coin coin = kit.wallet().getBalance();

        System.out.println("There are" + coin.getValue() + " Satoshi in the Wallet.");
    }

    @Test
    void generateNewAddress() {
        ECKey ecKey = new ECKey();
        DumpedPrivateKey privateKey = ecKey.getPrivateKeyEncoded(TestNet3Params.get());
        String privateKeyString = privateKey.toBase58();
        ecKey = ecKey.decompress();
        String publicKeyString = LegacyAddress.fromKey(TestNet3Params.get(), ecKey).toString();

        System.out.println("Private key: " + privateKeyString);
        System.out.println("Public key: " + publicKeyString);
    }

    @Test
    void sendCoins() throws InsufficientMoneyException {
        // Starting bitcoinj, download the block headers
        kit = new WalletAppKit(params, new File("."), filePrefix);
        kit.startAsync();
        kit.awaitRunning();

        // Determine address to send coins to
        Address forwardingAddress = LegacyAddress.fromBase58(params, "mfXVoph5jZhyp76W8B8FBRvxGNYTaA98Jy");

        // Create ECKey
        String privateKeyString = "cVrYUWwfMCVN1tLnarqfRE3kG3ipCv73Cvv8V5K4Sq92EHt9YPPs";
        DumpedPrivateKey privateKey = DumpedPrivateKey.fromBase58(TestNet3Params.get(), privateKeyString);
        ECKey ecKey = privateKey.getKey();

        // Add ECKey to wallet
        kit.wallet().importKey(ecKey);
        Coin coin = kit.wallet().getBalance();
        Coin coin1 = kit.wallet().getBalance(Wallet.BalanceType.AVAILABLE);

        // Print balance
        System.out.println("There are " + coin.getValue() + " Satoshi in the Wallet.");
        System.out.println("There are " + coin1.getValue() + " Satoshi available in the Wallet.");

        // Send coins
        Coin coinsToSend = Coin.valueOf(700); //500 ist zu wenig, 800 war zu viel -> missing 0.000008 BTC
        //Coin coinsToSend = Coin.valueOf(200000);

        SendRequest request = SendRequest.to(forwardingAddress, coinsToSend);
        kit.wallet().completeTx(request);
        kit.wallet().commitTx(request.tx);
        kit.peerGroup().broadcastTransaction(request.tx);
        System.out.println("Finished");
    }

}
