package org.example.model;

public class FixedAmountDiscount extends Discount {
    private final double discountAmount;
    public FixedAmountDiscount(String code, double amount) {
        super(code);
        this.discountAmount = amount;
    }

    @Override
    public double apply(double originalAmount) {
        return Math.max(0,originalAmount - discountAmount);
    }
}
