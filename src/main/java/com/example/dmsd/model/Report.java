package com.example.dmsd.model;

import java.math.BigDecimal;

public class Report {

    private String locationName;
    private String serviceName;
    private BigDecimal revenue;

    public Report(String locationName, String serviceName, BigDecimal revenue) {
        this.locationName = locationName;
        this.serviceName = serviceName;
        this.revenue = revenue;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
}