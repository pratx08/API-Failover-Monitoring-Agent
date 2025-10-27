package com.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class OrdersController {

    @GetMapping("/orders")
    public Map<String, Object> getOrders() {
        Map<String, Object> response = new HashMap<>();

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Random random = new Random();

        // Simulate a small list of realistic orders
        List<Map<String, Object>> orders = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            Map<String, Object> order = new HashMap<>();
            order.put("orderId", "ORD-" + (1000 + i));
            order.put("customerName", getRandomCustomerName(random));
            order.put("product", getRandomProduct(random));
            order.put("quantity", random.nextInt(3) + 1);
            order.put("amountUSD", String.format("%.2f", 50 + (random.nextDouble() * 450)));
            order.put("status", getRandomStatus(random));
            order.put("orderDate", generateRandomDate());
            order.put("expectedDelivery", generateFutureDate());
            orders.add(order);
        }

        response.put("server", "Order Details API");
        response.put("region", "us-east-1");
        response.put("timestamp", currentTime);
        response.put("totalOrders", orders.size());
        response.put("orders", orders);

        return response;
    }

    // ---------- Helper Methods ----------

    private String getRandomCustomerName(Random random) {
        String[] names = {"John Doe", "Ava Smith", "Liam Brown", "Sophia Patel", "Michael Johnson"};
        return names[random.nextInt(names.length)];
    }

    private String getRandomProduct(Random random) {
        String[] products = {"Wireless Headphones", "Bluetooth Speaker", "Smartwatch", "Gaming Mouse", "Portable SSD"};
        return products[random.nextInt(products.length)];
    }

    private String getRandomStatus(Random random) {
        String[] statuses = {"Processing", "Shipped", "Delivered", "Cancelled"};
        return statuses[random.nextInt(statuses.length)];
    }

    private String generateRandomDate() {
        long now = System.currentTimeMillis();
        long randomPast = now - (long) (Math.random() * 7 * 24 * 60 * 60 * 1000); // up to 7 days ago
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(randomPast));
    }

    private String generateFutureDate() {
        long now = System.currentTimeMillis();
        long randomFuture = now + (long) (Math.random() * 5 * 24 * 60 * 60 * 1000); // 0–5 days in future
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(randomFuture));
    }
}
