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

    private String creditCard;

    private String paymentMethod;

    private String locationName;
    private String vehicleType;

    private String CustomerName;
    private String ServiceName;
    private Services services;
    private LocalDate invoiceDate;

    public Appointment() {}

    public Appointment(int appointmentId, int customerId, int vehicleId, int locationId, String technicianSsn, LocalDate appointmentDate, String status, Double total_charge, int serviceId, String creditCard, String paymentMethod, String locationName, String vehicleType, String customerName, String serviceName, Services services, LocalDate invoiceDate) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.locationId = locationId;
        this.technicianSsn = technicianSsn;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.total_charge = total_charge;
        this.serviceId = serviceId;
        this.creditCard = creditCard;
        this.paymentMethod = paymentMethod;
        this.locationName = locationName;
        this.vehicleType = vehicleType;
        CustomerName = customerName;
        ServiceName = serviceName;
        this.services = services;
        this.invoiceDate = invoiceDate;
    }

    // getters and setters


    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCreditCard () {
            return creditCard;
        }

        public void setCreditCard (String creditCard){
            this.creditCard = creditCard;
        }

        public String getPaymentMethod () {
            return paymentMethod;
        }

        public void setPaymentMethod (String paymentMethod){
            this.paymentMethod = paymentMethod;
        }

        public int getServiceId () {
            return serviceId;
        }

        public void setServiceId ( int serviceId){
            this.serviceId = serviceId;
        }

        public int getAppointmentId () {
            return appointmentId;
        }

        public void setAppointmentId ( int appointmentId){
            this.appointmentId = appointmentId;
        }

        public int getCustomerId () {
            return customerId;
        }

        public void setCustomerId ( int customerId){
            this.customerId = customerId;
        }

        public int getVehicleId () {
            return vehicleId;
        }

        public void setVehicleId ( int vehicleId){
            this.vehicleId = vehicleId;
        }

        public int getLocationId () {
            return locationId;
        }

        public void setLocationId ( int locationId){
            this.locationId = locationId;
        }

        public String getTechnicianSsn () {
            return technicianSsn;
        }

        public void setTechnicianSsn (String technicianSsn){
            this.technicianSsn = technicianSsn;
        }

        public LocalDate getAppointmentDate () {
            return appointmentDate;
        }

        public void setAppointmentDate (LocalDate appointmentDate){
            this.appointmentDate = appointmentDate;
        }

        public String getStatus () {
            return status;
        }

        public void setStatus (String status){
            this.status = status;
        }

        public Double getTotal_charge () {
            return total_charge;
        }

        public void setTotal_charge (Double total_charge){
            this.total_charge = total_charge;
        }

    }
