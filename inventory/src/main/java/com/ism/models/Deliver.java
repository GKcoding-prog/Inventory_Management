package com.ism.models;

import java.util.Date;

public class Deliver {
    private final long productId;
    private final long supplierId;
    private final Date deliveryDate;

    public Deliver(long productId, long supplierId, Date deliveryDate) {
        this.productId = productId;
        this.supplierId = supplierId;
        this.deliveryDate = deliveryDate;
    }

    public long getProductId() { return productId; }
    public long getSupplierId() { return supplierId; }
    public Date getDeliveryDate() { return deliveryDate; }
}
