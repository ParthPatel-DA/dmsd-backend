package com.example.dmsd.controllers;

import com.example.dmsd.mapper.PendingServiceRowMapper;
import com.example.dmsd.mapper.Report3RowMapper;
import com.example.dmsd.mapper.ReportRowMapper;
import com.example.dmsd.model.CommonResponse;
import com.example.dmsd.model.PendingService;
import com.example.dmsd.model.Report3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/pending-services")
    public ResponseEntity<CommonResponse> getPendingServices(@PathParam("date") String date, @PathParam("location") String location) {
        String sql = "SELECT COUNT(s.service_id) as pending_count, l.lname, s.sname " +
                "FROM APPOINTMENTS a " +
                "JOIN LOCATIONS l ON a.locid = l.location_id " +
                "JOIN APPOINTMENTS_SERVICES_INVOICES asi ON a.appointment_id = asi.appointment_id " +
                "JOIN SERVICES s ON asi.service_id = s.service_id " +
                "WHERE (a.status = 'DROPPED' OR a.status = 'IN_PROGRESS') AND a.appointment_date = ? AND a.locid = ? GROUP BY s.service_id ORDER BY l.location_id;";
        try {
            List<PendingService> services = jdbcTemplate.query(sql, new Object[]{date, location}, new PendingServiceRowMapper());
            return new ResponseEntity<>(new CommonResponse(services, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching data"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping("/revenue/{startDate},{endDate}")
    public ResponseEntity<CommonResponse> getRevenueByLocationAndServiceType(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate){

        String sql = "SELECT l.lname, s.sname, SUM(ai.price) AS revenue FROM APPOINTMENTS a JOIN LOCATIONS l ON a.locid = l.location_id JOIN APPOINTMENTS_SERVICES_INVOICES ai ON a.appointment_id = ai.appointment_id JOIN SERVICES s ON ai.service_id = s.service_id JOIN INVOICES i ON ai.invoice_id = i.invoice_id WHERE a.status = 'DONE' AND a.appointment_date BETWEEN ? AND ? GROUP BY l.lname, s.sname order by l.lname";
        try {
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, startDate, endDate);
            return new ResponseEntity<>(new CommonResponse(results, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching data"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/report-top-3")
    public ResponseEntity<CommonResponse> getTop3LocationsByRevenue() {
        String query = "SELECT l.lname, SUM(ai.price) AS revenue FROM APPOINTMENTS a JOIN LOCATIONS l ON a.locid = l.location_id JOIN APPOINTMENTS_SERVICES_INVOICES ai ON a.appointment_id = ai.appointment_id JOIN SERVICES s ON ai.service_id = s.service_id JOIN INVOICES i ON ai.invoice_id = i.invoice_id WHERE a.status = 'DONE' GROUP BY l.lname ORDER BY revenue DESC limit 3";

        try {
            List<Report3> report3List = jdbcTemplate.query(query, new Report3RowMapper());
            return new ResponseEntity<>(new CommonResponse(report3List, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve report"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
