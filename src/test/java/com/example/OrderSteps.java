
package com.example;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderSteps {
    private OrderService orderService;
    private Order order;

    @Before
    public void setup() {
        orderService = new OrderService();
    }

    @Given("no promotions are applied")
    public void no_promotions_are_applied() {
        // Handled by @Before
    }

    @Given("the threshold discount promotion is configured:")
    public void the_threshold_discount_promotion_is_configured(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        double threshold = Double.parseDouble(rows.get(0).get("threshold"));
        double discount = Double.parseDouble(rows.get(0).get("discount"));
        orderService.addPromotion(new ThresholdPromotion(threshold, discount));
    }

    @Given("the buy one get one promotion for cosmetics is active")
    public void the_buy_one_get_one_promotion_for_cosmetics_is_active() {
        orderService.addPromotion(new BuyOneGetOnePromotion("cosmetics"));
    }

    @When("a customer places an order with:")
    public void a_customer_places_an_order_with(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        List<OrderItem> items = new java.util.ArrayList<>();
        for (Map<String, String> columns : rows) {
            String category = columns.get("category");
            if (category == null) {
                category = ""; // Default category if not provided
            }
            Product product = new Product(
                columns.get("productName"),
                category,
                Double.parseDouble(columns.get("unitPrice"))
            );
            items.add(new OrderItem(product, Integer.parseInt(columns.get("quantity"))));
        }
        order = orderService.calculateOrder(items);
    }

    @Then("the order summary should be:")
    public void the_order_summary_should_be(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> expected = rows.get(0);

        if (expected.containsKey("originalAmount")) {
            assertEquals(Double.parseDouble(expected.get("originalAmount")), order.getOriginalAmount());
        }
        if (expected.containsKey("discount")) {
            assertEquals(Double.parseDouble(expected.get("discount")), order.getDiscount());
        }
        if (expected.containsKey("totalAmount")) {
            assertEquals(Double.parseDouble(expected.get("totalAmount")), order.getTotalAmount());
        }
    }

    @Then("the customer should receive:")
    public void the_customer_should_receive(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> expectedItems = dataTable.asMaps(String.class, String.class);

        assertEquals(expectedItems.size(), order.getItems().size());

        for (int i = 0; i < expectedItems.size(); i++) {
            Map<String, String> expected = expectedItems.get(i);
            OrderItem actual = order.getItems().get(i);

            assertEquals(expected.get("productName"), actual.getProduct().getName());
            assertEquals(Integer.parseInt(expected.get("quantity")), actual.getQuantity());
        }
    }

    @Given("the Double {word} promotion is active, where for the same product with a quantity of {int} or more, a {int}% discount is applied to the total price of each batch of {int}")
    public void the_double_promotion_is_active_where_for_the_same_product_with_a_quantity_of_or_more_a_discount_is_applied_to_the_total_price_of_each_batch_of(String promotionName, Integer quantityThreshold, Integer discountPercentage, Integer batchSize) {
        // Write code here that turns the phrase above into concrete actions
        orderService.addPromotion(new Double11Promotion(quantityThreshold, discountPercentage, batchSize));
    }
}
