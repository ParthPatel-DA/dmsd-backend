package com.example.dmsd.model;

public class Location {
    private int locationId;
    private String managerSsn;
    private String employeeSsn;
    private String locationName;
    private String address;

    // Constructor
    public Location(int locationId, String managerSsn, String employeeSsn, String locationName, String address) {
        this.locationId = locationId;
        this.managerSsn = managerSsn;
        this.employeeSsn = employeeSsn;
        this.locationName = locationName;
        this.address = address;
    }

    public Location() {

    }

    // Getters and Setters
    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getManagerSsn() {
        return managerSsn;
    }

    public void setManagerSsn(String managerSsn) {
        this.managerSsn = managerSsn;
    }

    public String getEmployeeSsn() {
        return employeeSsn;
    }

    public void setEmployeeSsn(String employeeSsn) {
        this.employeeSsn = employeeSsn;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
