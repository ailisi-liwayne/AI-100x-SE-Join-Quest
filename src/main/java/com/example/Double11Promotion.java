package com.example;

import java.util.List;

public class Double11Promotion implements Promotion {
    private int quantityThreshold;
    private double discountPercentage;
    private int batchSize;

    public Double11Promotion(int quantityThreshold, double discountPercentage, int batchSize) {
        this.quantityThreshold = quantityThreshold;
        this.discountPercentage = discountPercentage;
        this.batchSize = batchSize;
    }

    @Override
    public void apply(Order order) {
        for (OrderItem item : order.getItems()) {
            if (item.getQuantity() >= quantityThreshold) {
                int numberOfBatches = item.getQuantity() / batchSize;
                double discountPerBatch = item.getProduct().getUnitPrice() * batchSize * (discountPercentage / 100.0);
                order.addDiscount(numberOfBatches * discountPerBatch);
            }
        }
    }
}
