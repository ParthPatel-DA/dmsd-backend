package com.example.dmsd.model;

import org.springframework.jdbc.core.RowCallbackHandler;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Report implements RowCallbackHandler {

    private String locationName;
    private String serviceName;

    private String revenue;

    public Report() {
        this.locationName = locationName;
        this.serviceName = serviceName;
        this.revenue = revenue;
    }



// constructor, getters, and setters



    public Report(String locationName, String serviceName, String revenue) {
        this.locationName = locationName;
        this.serviceName = serviceName;
        this.revenue = revenue;
    }

    public void setRevenue(String revenue) {
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

    public String getRevenue() {
        return revenue;
    }

    @Override
    public void processRow(ResultSet rs) throws SQLException {

    }
}