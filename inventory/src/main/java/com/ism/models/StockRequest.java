package com.ism.models;

import java.util.Date;

public class StockRequest {
    private long reqId;
    private final long supplierId;
    private final long userId;
    private final long quantityReq;
    private final String productReq;
    private final Date dateReq;
    private final String status;

    public StockRequest(long reqId, long supplierId, long userId, long quantityReq, String productReq, Date dateReq, String status) {
        this.reqId = reqId;
        this.supplierId = supplierId;
        this.userId = userId;
        this.quantityReq = quantityReq;
        this.productReq = productReq;
        this.dateReq = dateReq;
        this.status = status;
    }

    public void setReqId(long reqId) {
        this.reqId = reqId;
    }

    public long getRequestId() { return reqId; }
    public long getSupplierId() { return supplierId; }
    public long getUserId() { return userId; }
    public int getQuantity() { return (int) quantityReq; }
    public String getProductName() { return productReq; }
    public Date getDateReq() { return dateReq; }
    public String getStatus() { return status; }
    public String getRequestedBy() { return "User" + userId; }
}
