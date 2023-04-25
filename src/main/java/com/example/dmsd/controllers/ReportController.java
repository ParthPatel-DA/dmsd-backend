package com.example.dmsd.controllers;

import com.example.dmsd.mapper.ReportRowMapper;
import com.example.dmsd.model.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report/")
public class ReportController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/revenue/{startDate},{endDate}")
    public ResponseEntity<CommonResponse> getRevenueByLocationAndServiceType(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate){

        String sql = "SELECT l.lname, s.sname, SUM(ai.price) AS revenue FROM APPOINTMENTS a JOIN LOCATIONS l ON a.locid = l.location_id JOIN APPOINTMENTS_SERVICES_INVOICES ai ON a.appointment_id = ai.appointment_id JOIN SERVICES s ON ai.service_id = s.service_id JOIN INVOICES i ON ai.invoice_id = i.invoice_id WHERE a.appointment_date BETWEEN ? AND ? GROUP BY l.lname, s.sname";
        try {
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, startDate, endDate);
            return new ResponseEntity<>(new CommonResponse(results, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching data"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }




}
