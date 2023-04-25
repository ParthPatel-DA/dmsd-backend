package com.example.dmsd.model;
public class Report3 {
    private String locationName;
    private Integer revenue;

    public Report3() {}

    public Report3(String locationName, int revenue) {
        this.locationName = locationName;
        this.revenue = revenue;
    }

    public String getLocationName(String lname) {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getRevenue(double revenue) {
        return this.revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }
}
