package com.example.dmsd.model;


public class Customer {
    private Person person;
    private String creditCard;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    // Constructor
    public Customer(Person person, String creditCard) {
        this.person = person;
        this.creditCard = creditCard;
    }

    public Customer(Person person) {
        this.person = person;
    }

}