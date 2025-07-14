package com.ism.models;

public class Appear {
    private final long productId;
    private final long reqId;

    public Appear(long productId, long reqId) {
        this.productId = productId;
        this.reqId = reqId;
    }

    public long getProductId() { return productId; }
    public long getReqId() { return reqId; }
}
