package com.example.dmsd.controllers;

import com.example.dmsd.mapper.EmployeeRowMapper;
import com.example.dmsd.model.CommonResponse;
import com.example.dmsd.model.Employee;
import com.example.dmsd.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ManagerController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Create a new technician employee record
    @PostMapping("")
    public ResponseEntity<CommonResponse> createTechnician(@RequestBody Employee employee) {
        try {
            // Insert the employee record into the EMPLOYEE and PERSON tables
            jdbcTemplate.update("INSERT INTO PERSON (firstName, pass, address, email, person_type, telephone) VALUES (?, ?, ?, ?, ?, ?)",
                    employee.getPerson().getFirstName(), employee.getPerson().getPass(), employee.getPerson().getAddress(),
                    employee.getPerson().getEmail(), Person.PersonType.EMPLOYEE.name(), employee.getPerson().getTelephone());

            int personId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

            jdbcTemplate.update("INSERT INTO EMPLOYEE (ssn, person_id, salary, location_id, job_type, commision_percentage, expertise) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    employee.getSsn(), personId, employee.getSalary(), employee.getLocation_id(), Employee.JobType.MANAGER.name(),
                    employee.getCommisionPercentage(), employee.getExpertise());

            jdbcTemplate.update("UPDATE dmsd.LOCATIONS SET mssn = ? WHERE location_id=?",
                    employee.getSsn(), employee.getLocation_id());

            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Employee record created successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error creating employee record"+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an existing technician employee record
    @PutMapping("/{ssn}")
    public ResponseEntity<CommonResponse> updateTechnician(@PathVariable String ssn, @RequestBody Employee employee) {
        try {
            // Update the employee record in the EMPLOYEE and PERSON tables
            jdbcTemplate.update("UPDATE PERSON SET pass=?, firstName=?, address=?, email=?, person_type=?, telephone=? WHERE person_id = (SELECT person_id FROM EMPLOYEE WHERE ssn = ?)",
                    employee.getPerson().getPass(), employee.getPerson().getFirstName(), employee.getPerson().getAddress(),
                    employee.getPerson().getEmail(), employee.getPerson().getPersonType().name(), employee.getPerson().getTelephone(), ssn);

            jdbcTemplate.update("UPDATE EMPLOYEE SET salary=?, location_id=?, job_type=?, commission_percentage=?, expertise=? WHERE ssn = ?",
                    employee.getSalary(), employee.getLocation_id(), employee.getJob_type().name(),
                    employee.getCommisionPercentage(), employee.getExpertise(), ssn);

            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Employee record updated successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error updating employee record"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a technician employee record
    @DeleteMapping("/{ssn}")
    public ResponseEntity<CommonResponse> deleteTechnician(@PathVariable String ssn) {
        try {
            // Delete the employee record from the EMPLOYEE and PERSON tables
            jdbcTemplate.update("DELETE FROM EMPLOYEE WHERE ssn = ?", ssn);

            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Employee record deleted successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error deleting employee record"), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<CommonResponse> getTechnicianById(@PathVariable("id") String id) {
        String query = "SELECT * FROM PERSON p NATURAL JOIN EMPLOYEE e WHERE e.job_type = 'MANAGER' AND p.person_id = ?";

        try {
            Employee technician = jdbcTemplate.queryForObject(query, new Object[]{id}, new EmployeeRowMapper());

            if (technician != null) {
                return new ResponseEntity<>(new CommonResponse(technician, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "Technician not found"), HttpStatus.NOT_FOUND);
            }
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "Technician not found"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to login user"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getAllTechnicians() {
        try {
            String query = "SELECT p.*,e.*,l.lname " +
                    "FROM (person p NATURAL JOIN employee e)  " +
                    "LEFT JOIN locations l on e.location_id = l.location_id " +
                    "WHERE p.person_type = 'EMPLOYEE' AND e.job_type = 'MANAGER';";;
            List<Employee> technicians = jdbcTemplate.query(query, new EmployeeRowMapper());
            return new ResponseEntity<>(new CommonResponse(technicians, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while fetching technicians"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}