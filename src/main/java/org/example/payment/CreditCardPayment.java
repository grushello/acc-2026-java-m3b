package org.example.payment;

import org.example.model.PaymentResult;

public class CreditCardPayment extends PaymentMethod {
    private final String cardNumber;
    private final String cardHolderName;

    public CreditCardPayment(String cardNumber, String cardHolderName) {
        super("Credit Card");

        // there was a todo to perform basic validations in processPayment
        // but I would prefer to place them in the constructor, so here you go
        if(cardNumber == null || cardNumber.length() < 4){
            throw new IllegalArgumentException("Card number cannot be shorter than 4 digits");
        }
        if(cardHolderName == null || cardHolderName.isEmpty()){
            throw new IllegalArgumentException("Cardholder name cannot be empty");
        }
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    @Override
    public PaymentResult processPayment(double amount) {
        return new PaymentResult(true, "Paid " + amount + " using credit card ending with " + cardNumber.substring(cardNumber.length() - 4));
    }
}
