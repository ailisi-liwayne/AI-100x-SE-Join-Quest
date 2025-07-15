
package com.example;

public class ThresholdPromotion implements Promotion {
    private double threshold;
    private double discount;

    public ThresholdPromotion(double threshold, double discount) {
        this.threshold = threshold;
        this.discount = discount;
    }

    @Override
    public double apply(double originalAmount) {
        if (originalAmount >= threshold) {
            return discount;
        }
        return 0;
    }
}
