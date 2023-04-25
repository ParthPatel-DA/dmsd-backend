package com.example.dmsd.controllers;


import com.example.dmsd.mapper.ServicesRowMapper;
import com.example.dmsd.model.CommonResponse;
import com.example.dmsd.model.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Service;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/services")
public class ServicesController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Create a new service
    @PostMapping("")
    public ResponseEntity<CommonResponse> createService(@RequestBody Services service) {
        try {
            String sql = "INSERT INTO SERVICES (skillid, sname, labor_price, addcharge) VALUES (?, ?, ?, ?)";
            int rows = jdbcTemplate.update(sql, service.getSkillId(), service.getServiceName(), service.getLaborPrice(), service.getAddCharge());

            int serviceID = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

            for(Integer partId : service.getPartIds()) {
                String query = "INSERT INTO SERVICES_PARTS (service_id, part_id ) VALUES (?, ?)";
                jdbcTemplate.update(query, serviceID, partId);
            }

            if (rows == 1) {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Service created successfully"), HttpStatus.OK);
            } else {
                throw new SQLException("Failed to create service");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error creating service: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a service by ID
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getServiceById(@PathVariable("id") int id) {
        try {
            String sql = "SELECT * FROM SERVICES WHERE service_id = ?";
            Services service = jdbcTemplate.queryForObject(sql, new Object[]{id}, new ServicesRowMapper());
            return new ResponseEntity<>(new CommonResponse(service, HttpStatus.OK.value(), "Service Fetched successfully"), HttpStatus.OK);

        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "Failed Services fetched by id."), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching by ID: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Update a service by ID
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateService(@PathVariable("id") int id, @RequestBody Services service) {
        try {
            String sql = "UPDATE SERVICES SET skillid = ?, sname = ?, labor_price = ?, addcharge = ? WHERE service_id = ?";
            int rows = jdbcTemplate.update(sql, service.getSkillId(), service.getServiceName(), service.getLaborPrice(), service.getAddCharge(), id);
            if (rows == 1) {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Service updated successfully"), HttpStatus.OK);
            } else {
                throw new SQLException("Failed to update service");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error updating service: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a service by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteService(@PathVariable("id") int id) {
        try {
            String sql = "DELETE FROM SERVICES WHERE service_id = ?";
            int rows = jdbcTemplate.update(sql, id);
            if (rows == 1) {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Service deleted successfully"), HttpStatus.OK);
            } else {
                throw new SQLException("Failed to delete service");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error deleting service: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all services
    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getAllServices() {
        try {
            String sql = "SELECT l.lname, SUM(ai.price) AS revenue \n" +
                    "FROM APPOINTMENTS a \n" +
                    "JOIN LOCATIONS l ON a.locid = l.location_id \n" +
                    "JOIN APPOINTMENTS_SERVICES_INVOICES ai ON a.appointment_id = ai.appointment_id \n" +
                    "JOIN SERVICES s ON ai.service_id = s.service_id \n" +
                    "JOIN INVOICES i ON ai.invoice_id = i.invoice_id \n" +
                    "GROUP BY l.lname\n" +
                    "ORDER BY revenue DESC limit 3;";
            List<Services> services = jdbcTemplate.query(sql, new ServicesRowMapper());
            return new ResponseEntity<>(new CommonResponse(services, HttpStatus.OK.value(), "Fetched all Services."), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error Fetching service. " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
