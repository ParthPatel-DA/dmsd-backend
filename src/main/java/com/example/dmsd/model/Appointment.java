package com.example.dmsd.model;

import java.time.LocalDate;

public class Appointment {
    private int appointmentId;
    private int customerId;
    private int vehicleId;
    private int locationId;
    private String technicianSsn;
    private LocalDate appointmentDate;
    private String status;
    private Double total_charge;
    private int serviceId;

    public Appointment() {}

    public Appointment(int appointmentId, int customerId, int vehicleId, int locationId, String technicianSsn, LocalDate appointmentDate, String status) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.locationId = locationId;
        this.technicianSsn = technicianSsn;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    // getters and setters

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getTechnicianSsn() {
        return technicianSsn;
    }

    public void setTechnicianSsn(String technicianSsn) {
        this.technicianSsn = technicianSsn;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotal_charge() {
        return total_charge;
    }

    public void setTotal_charge(Double total_charge) {
        this.total_charge = total_charge;
    }
}