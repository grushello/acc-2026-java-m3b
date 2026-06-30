package org.example.payment;

import org.example.model.Order;
import org.example.model.PaymentResult;

public class PaymentProcessor {
    public PaymentResult process(Order order, PaymentMethod paymentMethod){
        if(order.isPaid())
        {
            throw new IllegalArgumentException("Cannot pay already paid order");
        }
        if(order.getItems().isEmpty())
        {
            throw new IllegalArgumentException("Cannot pay empty order");
        }

        PaymentResult result = paymentMethod.pay(order.calculateTotal());

        if(result.isSuccessful()){
            order.markAsPaid();
        }

        return result;
    }
}
