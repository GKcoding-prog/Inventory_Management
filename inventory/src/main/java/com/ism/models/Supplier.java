package com.ism.models;

public class Supplier {
    private long supplierId;
    private String supplierFname;
    private String contactInfo;

    public Supplier(long supplierId, String supplierFname, String contactInfo) {
        this.supplierId = supplierId;
        this.supplierFname = supplierFname;
        this.contactInfo = contactInfo;
    }

    public long getSupplierId() { return supplierId; }
    public String getSupplierFname() { return supplierFname; }
    public String getContactInfo() { return contactInfo; }

    public void setSupplierId(long long1) {
        this.supplierId = long1;
    }

    public void setSupplierFname(String supplierFname) {
        this.supplierFname = supplierFname;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    }
// End of Supplier class
