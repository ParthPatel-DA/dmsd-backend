package com.example.dmsd.model;
public class Report3 {
    private String locationName;
    private Double revenue;

    public Report3() {}

    public Report3(String locationName, Double revenue) {
        this.locationName = locationName;
        this.revenue = revenue;
    }

    public String getLocationName() {
        return locationName != null ? locationName : "";
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Double getRevenue() {
        return revenue != null ? revenue : 0.0;
    }


    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }
}
