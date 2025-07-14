package com.ism.controllers;

public class ProductRow {
    private final String name;
    private final String category;
    private final int quantity;
    private final String supplier;
    public ProductRow(String name, String category, int quantity, String supplier) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.supplier = supplier;
    }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public String getSupplier() { return supplier; }
}
