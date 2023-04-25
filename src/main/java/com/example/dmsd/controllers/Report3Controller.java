package com.example.dmsd.controllers;

import com.example.dmsd.mapper.Report3RowMapper;
import com.example.dmsd.model.CommonResponse;
import com.example.dmsd.model.Report3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;
import java.util.List;

@RestController
@RequestMapping("/api/report3")
public class Report3Controller {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @GetMapping("/r3")
    public ResponseEntity<CommonResponse> getRevenueByLocation() {

        String sql1 ="SELECT l.lname, SUM(ai.price) AS revenue FROM APPOINTMENTS a"+
                "JOIN LOCATIONS l ON a.locid = l.location_id"+
        "JOIN APPOINTMENTS_SERVICES_INVOICES ai ON a.appointment_id = ai.appointment_id"+
        "JOIN SERVICES s ON ai.service_id = s.service_id"+
        "JOIN INVOICES i ON ai.invoice_id = i.invoice_id"+
        "GROUP BY l.lname"+
        "ORDER BY revenue DESC limit 3";

            List<Report3> report3List = jdbcTemplate.query(sql1, new Report3RowMapper());
            return new ResponseEntity<>(new CommonResponse(report3List, HttpStatus.OK.value(), "Success"), HttpStatus.OK);


        }

}
