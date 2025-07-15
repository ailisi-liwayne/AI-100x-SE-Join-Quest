

package com.example;

import java.util.List;
import java.util.ArrayList;

public class OrderService {
    private List<Promotion> promotions = new ArrayList<>();

    public void addPromotion(Promotion promotion) {
        this.promotions.add(promotion);
    }

    public Order calculateOrder(List<OrderItem> initialItems) {
        Order order = new Order();
        List<OrderItem> finalItems = new ArrayList<>(initialItems);

        // Apply item-based promotions first
        for (Promotion promotion : promotions) {
            if (promotion instanceof BuyOneGetOnePromotion) {
                finalItems = ((BuyOneGetOnePromotion) promotion).applyToItems(finalItems);
            }
        }
        order.setItems(finalItems);

        double originalAmount = 0;
        for (OrderItem item : initialItems) { // Calculate original amount based on initial items
            originalAmount += item.getProduct().getUnitPrice() * item.getQuantity();
        }
        order.setOriginalAmount(originalAmount);

        double totalDiscount = 0;
        for (Promotion promotion : promotions) {
            if (!(promotion instanceof BuyOneGetOnePromotion)) {
                totalDiscount += promotion.apply(originalAmount);
            }
        }
        order.setDiscount(totalDiscount);

        order.setTotalAmount(originalAmount - totalDiscount);

        return order;
    }
}

