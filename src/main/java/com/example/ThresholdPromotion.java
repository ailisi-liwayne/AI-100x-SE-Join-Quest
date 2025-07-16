
package com.example;

public class ThresholdPromotion implements Promotion {
    private double threshold;
    private double discount;

    public ThresholdPromotion(double threshold, double discount) {
        this.threshold = threshold;
        this.discount = discount;
    }

    @Override
    public void apply(Order order) {
        if (order.getOriginalAmount() >= threshold) {
            order.addDiscount(discount);
        }
    }
}
