

package com.example.order;

import java.util.List;
import java.util.ArrayList;

public class OrderService {
    private List<Promotion> promotions = new ArrayList<>();

    public void addPromotion(Promotion promotion) {
        this.promotions.add(promotion);
    }

    public Order calculateOrder(List<OrderItem> initialItems) {
        Order order = new Order();
        order.setItems(new ArrayList<>(initialItems)); // Initialize order with initial items

        // Calculate original amount before any item modifications
        double originalAmount = 0;
        for (OrderItem item : initialItems) {
            originalAmount += item.getProduct().getUnitPrice() * item.getQuantity();
        }
        order.setOriginalAmount(originalAmount);

        // Apply all promotions
        for (Promotion promotion : promotions) {
            promotion.apply(order);
        }

        // Calculate final total amount
        order.setTotalAmount(order.getOriginalAmount() - order.getDiscount());

        return order;
    }
}

