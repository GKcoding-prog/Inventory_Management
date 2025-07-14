package com.ism.models;

public class Product {
    private long productId;
    private long categoryId;
    private String productName;
    private double productPrice;
    private long productQuantity;

    public Product(long productId, long categoryId, String productName, double productPrice, long productQuantity) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }

    public long getCategoryId() { return categoryId; }
    public void setCategoryId(long categoryId) { this.categoryId = categoryId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getProductPrice() { return productPrice; }
    public void setProductPrice(double productPrice) { this.productPrice = productPrice; }

    public long getProductQuantity() { return productQuantity; }
    public void setProductQuantity(long productQuantity) { this.productQuantity = productQuantity; }
}
