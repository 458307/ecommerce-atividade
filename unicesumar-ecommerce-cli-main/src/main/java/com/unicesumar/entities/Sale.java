package com.unicesumar.entities;

public class Sale {
    private int id;
    private String customerName;
    private double totalPrice;
    private String paymentType;

    public Sale(int id, String customerName, double totalPrice, String paymentType) {
        this.id = id;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
        this.paymentType = paymentType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @Override
public String toString() {
    return String.format("Venda ID: %d | Cliente: %s | Total: R$ %.2f | Pagamento: %s",
            id, customerName, totalPrice, paymentType);
}
}