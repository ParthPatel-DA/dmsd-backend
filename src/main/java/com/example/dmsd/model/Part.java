package com.example.dmsd.model;

public class Part {
    private int partId;
//    private int serviceId;
    private int quantity;
    private String status;
    private String pname;
    private int retailPrice;

    public Part() {
    }

    public Part(int partId, int quantity, String status, String pname, int retailPrice) {
        this.partId = partId;
//        this.serviceId = serviceId;
        this.quantity = quantity;
        this.status = status;
        this.pname = pname;
        this.retailPrice = retailPrice;
    }

    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }
//
//    public int getServiceId() {
//        return serviceId;
//    }
//
//    public void setServiceId(int serviceId) {
//        this.serviceId = serviceId;
//    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(int retailPrice) {
        this.retailPrice = retailPrice;
    }
}
