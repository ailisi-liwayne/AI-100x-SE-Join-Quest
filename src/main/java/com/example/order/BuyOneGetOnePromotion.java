

package com.example.order;

import java.util.List;
import java.util.ArrayList;

public class BuyOneGetOnePromotion implements Promotion {
    private String category;

    public BuyOneGetOnePromotion(String category) {
        this.category = category;
    }

    @Override
    public void apply(Order order) {
        List<OrderItem> newItems = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            if (item.getProduct().getCategory() != null && item.getProduct().getCategory().equals(category)) {
                // For buy one get one, add one free item for each item purchased
                newItems.add(new OrderItem(item.getProduct(), item.getQuantity() + 1));
            } else {
                newItems.add(item);
            }
        }
        order.setItems(newItems);
    }
}

