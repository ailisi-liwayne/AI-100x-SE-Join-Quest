

package com.example;

import java.util.List;
import java.util.ArrayList;

public class BuyOneGetOnePromotion implements Promotion {
    private String category;

    public BuyOneGetOnePromotion(String category) {
        this.category = category;
    }

    @Override
    public double apply(double originalAmount) {
        return 0; // This promotion does not directly apply a discount to the total amount
    }

    public List<OrderItem> applyToItems(List<OrderItem> originalItems) {
        List<OrderItem> newItems = new ArrayList<>();
        for (OrderItem item : originalItems) {
            if (item.getProduct().getCategory() != null && item.getProduct().getCategory().equals(category)) {
                // For buy one get one, add one free item for each item purchased
                newItems.add(new OrderItem(item.getProduct(), item.getQuantity() + 1));
            } else {
                newItems.add(item);
            }
        }
        return newItems;
    }
}

