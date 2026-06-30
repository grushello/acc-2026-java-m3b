package org.example.model;

public class Tax {
    private final double taxRate;

    public Tax(double taxRate){
        this.taxRate = taxRate;
    }
    public double apply(double originalAmount){
        return originalAmount + originalAmount * taxRate;
    }
}
