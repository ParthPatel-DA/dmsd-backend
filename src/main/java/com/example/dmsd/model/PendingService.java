package com.example.dmsd.model;

public class PendingService {
    private Double pending_count;
    private String locationName;
    private String serviceName;

    public PendingService() {
    }

    public PendingService(Double pending_count, String locationName, String serviceName) {
        this.pending_count = pending_count;
        this.locationName = locationName;
        this.serviceName = serviceName;
    }

    public Double getPending_count() {
        return pending_count;
    }

    public void setPending_count(Double pending_count) {
        this.pending_count = pending_count;
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
}
