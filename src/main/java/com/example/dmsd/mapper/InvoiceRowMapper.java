package com.example.dmsd.mapper;

import com.example.dmsd.model.Invoice;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InvoiceRowMapper implements RowMapper<Invoice> {

    @Override
    public Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {
        Invoice invoice = new Invoice();
//        invoice.setInvoiceId(rs.getInt("invoice_id"));
//        invoice.setInvoiceDate(rs.getDate("invoice_date").toLocalDate());
//        invoice.setTotalCharge(rs.getInt("total_charge"));
        invoice.setPaymentMethod(rs.getString("payment_method"));
        invoice.setCreditcard(rs.getString("credit_card"));
        return invoice;
    }
}

