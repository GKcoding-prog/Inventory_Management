package com.ism.models;

import java.util.Date;

public class StockRequest {
    private final long reqId;
    private final long supplierId;
    private final long userId;
    private final long quantityReq;
    private final String productReq;
    private final Date dateReq;

    public StockRequest(long reqId, long supplierId, long userId, long quantityReq, String productReq, Date dateReq) {
        this.reqId = reqId;
        this.supplierId = supplierId;
        this.userId = userId;
        this.quantityReq = quantityReq;
        this.productReq = productReq;
        this.dateReq = dateReq;
    }

    public long getReqId() { return reqId; }
    public long getSupplierId() { return supplierId; }
    public long getUserId() { return userId; }
    public long getQuantityReq() { return quantityReq; }
    public String getProductReq() { return productReq; }
    public Date getDateReq() { return dateReq; }
}
