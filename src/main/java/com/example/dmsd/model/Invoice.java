package com.example.dmsd.model;

import java.time.LocalDate;

public class Invoice {
   // private int invoiceId;
    //private LocalDate invoiceDate;
   // private int totalCharge;
    private String paymentMethod;
    private String creditcard;

//    public int getInvoiceId() {
//        return invoiceId;
//    }

    public String getCreditcard() {
        return creditcard;
    }

    public void setCreditcard(String creditcard) {
        this.creditcard = creditcard;
    }

//    public void setInvoiceId(int invoiceId) {
//        this.invoiceId = invoiceId;
//    }
//
//    public LocalDate getInvoiceDate() {
//        return invoiceDate;
//    }
//
//    public void setInvoiceDate(LocalDate invoiceDate) {
//        this.invoiceDate = invoiceDate;
//    }
//
//    public int getTotalCharge() {
//        return totalCharge;
//    }
//
//    public void setTotalCharge(int totalCharge) {
//        this.totalCharge = totalCharge;
//    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // getters and setters
}

