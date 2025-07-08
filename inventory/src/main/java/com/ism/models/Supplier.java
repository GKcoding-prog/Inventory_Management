package com.ism.models;

public class Supplier {
    private final long supplierId;
    private final String supplierFname;
    private final String contactInfo;

    public Supplier(long supplierId, String supplierFname, String contactInfo) {
        this.supplierId = supplierId;
        this.supplierFname = supplierFname;
        this.contactInfo = contactInfo;
    }

    public long getSupplierId() { return supplierId; }
    public String getSupplierFname() { return supplierFname; }
    public String getContactInfo() { return contactInfo; }
}
