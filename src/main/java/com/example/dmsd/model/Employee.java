package com.example.dmsd.model;

public class Employee {
    private String ssn;
    private Long personId;
    private int salary;
    private String locationId;
    private String jobType;
    private int commissionPercentage;
    private String expertise;

    // Constructor
    public Employee(String ssn, Long personId, int salary, String locationId, String jobType, int commissionPercentage, String expertise) {
        this.ssn = ssn;
        this.personId = personId;
        this.salary = salary;
        this.locationId = locationId;
        this.jobType = jobType;
        this.commissionPercentage = commissionPercentage;
        this.expertise = expertise;
    }

    // Getters and Setters
    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public int getCommissionPercentage() {
        return commissionPercentage;
    }

    public void setCommissionPercentage(int commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }
}