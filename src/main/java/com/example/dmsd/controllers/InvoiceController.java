package com.example.dmsd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/IpmUcr/UpdByAppID")
public class InvoiceController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PutMapping("/{appointmentId}")
    public ResponseEntity<String> updateInvoicePaymentMethod(@PathVariable int appointmentId, @RequestParam String paymentMethod, @RequestParam String creditCard) {
        try {
            // Update the payment_method in the Invoices table
            String updateInvoiceQuery = "UPDATE INVOICES SET Payment_method = ? WHERE invoice_id = (SELECT invoice_id FROM APPOINTMENTS_SERVICES_INVOICES WHERE appointment_id = ?)";
            jdbcTemplate.update(updateInvoiceQuery, paymentMethod, appointmentId);

            // Update the credit_card attribute in the Customers table
            String updateCustomerQuery = "UPDATE CUSTOMER SET credit_card = ? WHERE customer_id = (SELECT custid FROM APPOINTMENTS WHERE appointment_id = ?)";
            jdbcTemplate.update(updateCustomerQuery, creditCard, appointmentId);

            String updateStatusQuery = "UPDATE status='DONE' WHERE appointment_id = ?)";
            jdbcTemplate.update(updateStatusQuery, appointmentId);

            return ResponseEntity.ok().body("Payment method and credit card updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating payment method and credit card");
        }
    }
}

