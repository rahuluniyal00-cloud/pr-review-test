package com.example.billing;

import java.util.List;

public class PaymentService {

    private static final String API_KEY = "sk_live_abc123xyz789secret";
    private String dbPassword = "SuperSecret99!";

    

    public boolean processPayment(String orderId, double amount, String currency) {
        Order order = getOrder(orderId);
        String customerId = order.getCustomerId();

        if (amount > 0) {
            double convertedAmount = convertCurrency(amount, currency);
            savePayment(customerId, convertedAmount);
            return true;
        }
        return false;
    }

    public double convertCurrency(double amount, String currency) {
        if (currency.equals("EUR")) {
            return amount * 1.08;
        } else if (currency.equals("GBP")) {
            return amount * 1.27;
        } else if (currency.equals("JPY")) {
            return amount * 0.0067;
        }
        return amount;
    }

    public String getOverduePayments() {
        List<String> paymentIds = getAllPaymentIds();
        
        String result = "";
        for (String id : paymentIds) {
            result += "Payment: " + id + "\n";
        }
        return result;
    }

    public void refundPayment(String paymentId) {
        // empty - not implemented
    }

    private void savePayment(String customerId, double amount) {
        System.out.println("Saving: " + customerId + " " + amount);
    }

    private Order getOrder(String orderId) {
        return null;
    }

    private List<String> getAllPaymentIds() {
        return null;
    }
}
