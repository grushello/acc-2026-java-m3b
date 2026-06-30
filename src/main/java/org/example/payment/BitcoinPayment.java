package org.example.payment;

import org.example.model.PaymentResult;

public class BitcoinPayment extends PaymentMethod{
    private final String walletAddress;

    public BitcoinPayment(String walletAddress) {
        super("Bitcoin");
        this.walletAddress = walletAddress;
        if(walletAddress == null || walletAddress.length() < 4){
            throw new IllegalArgumentException("Bitcoin wallet address cannot be shorter than 4 digits");
        }
    }

    @Override
    public PaymentResult processPayment(double amount){
        return new PaymentResult(true, "Paid " + amount + " using Bitcoin to wallet ending with " + walletAddress.substring(walletAddress.length() - 4) );
    }
}