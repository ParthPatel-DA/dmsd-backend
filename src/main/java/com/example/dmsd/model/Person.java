package com.example.dmsd.model;

public class Person {
    private Long id;
    private String pass;
    private String firstName;
    private String address;
    private String email;
    private PersonType person_type;
    private String telephone;
    private String token;

    public Person() {

    }

    // Constructor
    public Person(Long id, String pass, String firstName, String address, String email, String telephone) {
        this.id = id;
        this.pass = pass;
        this.firstName = firstName;
        this.address = address;
        this.email = email;
        this.telephone = telephone;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public PersonType getPersonType() {
        return person_type;
    }

    public void setPersonType(String person_type) {
        this.person_type = PersonType.valueOf(person_type);
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public enum PersonType {
        EMPLOYEE,
        CUSTOMER,
        ADMIN,
    }
}