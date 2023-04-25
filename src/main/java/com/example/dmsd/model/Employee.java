package com.example.dmsd.model;

public class Employee {
    private Person person;
    private String ssn;
    private Long person_id;
    private Long salary;
    private Long location_id;
    private JobType job_type;
    private Double commision_percentage;
    private String expertise;

    private String locationName;

    public Employee() {
    }

    public Employee(Person person) {
        this.person = person;
    }


    public Employee(Person person, String ssn, Long person_id, Long salary, Long location_id, JobType job_type, Double commision_percentage, String expertise, String locationName) {
        this.person = person;
        this.ssn = ssn;
        this.person_id = person_id;
        this.salary = salary;
        this.location_id = location_id;
        this.job_type = job_type;
        this.commision_percentage = commision_percentage;
        this.expertise = expertise;
        this.locationName = locationName;
    }

    public void setJob_type(JobType job_type) {
        this.job_type = job_type;
    }

    public Double getCommision_percentage() {
        return commision_percentage;
    }

    public void setCommision_percentage(Double commision_percentage) {
        this.commision_percentage = commision_percentage;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Long person_id) {
        this.person_id = person_id;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Long getLocation_id() {
        return location_id;
    }

    public void setLocation_id(Long location_id) {
        this.location_id = location_id;
    }

    public JobType getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = JobType.valueOf(job_type);
    }

    public Double getCommisionPercentage() {
        return commision_percentage;
    }

    public void setCommisionPercentage(Double commision_percentage) {
        this.commision_percentage = commision_percentage;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public enum JobType {
        TECHNICIAN,
        MANAGER,
    }
}
