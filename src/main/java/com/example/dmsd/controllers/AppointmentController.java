package com.example.dmsd.controllers;

import com.example.dmsd.mapper.AppointmentsRowMapper;
import com.example.dmsd.mapper.VehicleRowMapper;
import com.example.dmsd.model.Appointment;
import com.example.dmsd.model.CommonResponse;
import com.example.dmsd.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/appointments")
public class AppointmentController {

    private final JdbcTemplate jdbcTemplate;

    public AppointmentController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Create a new appointment
    @PostMapping("")
    public ResponseEntity<CommonResponse> createAppointment(@RequestBody Appointment appointment) {
        try {
            String sql = "INSERT INTO APPOINTMENTS (custid, vechid, locid, techssn, appointment_date, status) VALUES (?, ?, ?, ?, ?, ?)";
            int rows = jdbcTemplate.update(sql, appointment.getCustomerId(), appointment.getVehicleId(), appointment.getLocationId(), appointment.getTechnicianSsn(), appointment.getAppointmentDate(), appointment.getStatus());
            if (rows == 1) {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.CREATED.value(), "Appointment created successfully"), HttpStatus.OK);

            } else {
                throw new SQLException("Failed to create appointment");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error creating Appointment."+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Update an existing appointment
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateAppointment(@PathVariable("id") int id, @RequestBody Appointment appointment) {
        try {
            String sql = "UPDATE APPOINTMENTS SET custid = ?, vechid = ?, locid = ?, techssn = ?, appointment_date = ?, status = ? WHERE appointment_id = ?";
            int rows = jdbcTemplate.update(sql, appointment.getCustomerId(), appointment.getVehicleId(), appointment.getLocationId(), appointment.getTechnicianSsn(), appointment.getAppointmentDate(), appointment.getStatus(), id);
            if (rows == 1) {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Appointment Updated successfully"), HttpStatus.OK);


            } else {
                throw new SQLException("Failed to update appointment");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error Updating Appointment."+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Delete an existing appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteAppointment(@PathVariable("id") int id) {
        try {
            String sql = "DELETE FROM APPOINTMENTS WHERE appointment_id = ?";
            int rows = jdbcTemplate.update(sql, id);
            if (rows == 1) {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NO_CONTENT.value(), "Appointment Deleted successfully"), HttpStatus.OK);

            } else {
                throw new SQLException("Failed to delete appointment");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error Deleting Appointment."+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Get a single appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getAppointment(@PathVariable("id") int id) {
        try {
            String sql = "SELECT * FROM APPOINTMENTS WHERE appointment_id = ?";
            Appointment appointment = jdbcTemplate.queryForObject(sql, new AppointmentsRowMapper(), id);
            return new ResponseEntity<>(new CommonResponse(appointment, HttpStatus.OK.value(), "Appointment Fetched by id."), HttpStatus.OK);

        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "Failed finding Appointment."+e.getMessage()), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error Fetching Appointment by ID."+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Get a list of all appointments
    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getAllAppointments() {
        try {
            String sql = "SELECT * FROM APPOINTMENTS";
            List<Appointment> appointments = jdbcTemplate.query(sql, new AppointmentsRowMapper());
            return new ResponseEntity<>(new CommonResponse(appointments, HttpStatus.OK.value(), "All Appointments Fetched."), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching all Appointment."+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Get appointments by location ID
    @GetMapping("/loc/{locationId}")
    public ResponseEntity<CommonResponse> getAppointmentsByLocationId(@PathVariable("locationId") int locationId) {
        try {
            String sql = "SELECT * FROM appointments WHERE locid = ?";
            List<Appointment> appointments = jdbcTemplate.query(sql, new Object[]{locationId}, new AppointmentsRowMapper());
            return new ResponseEntity<>(new CommonResponse(appointments, HttpStatus.OK.value(), "All Appointments Fetched by location ID."), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching all Appointment by location ID."+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Get appointments by customer ID
    @GetMapping("/cust/{customerId}")
    public ResponseEntity<CommonResponse> getAppointmentsByCustomerId(@PathVariable("customerId") int customerId) {
        try {
            String sql = "SELECT * FROM appointments WHERE custid = ?";
            List<Appointment> appointments = jdbcTemplate.query(sql, new Object[]{customerId}, new AppointmentsRowMapper());
            return new ResponseEntity<>(new CommonResponse(appointments, HttpStatus.OK.value(), "All Appointments Fetched by Customer ID."), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching all Appointment by customer ID."+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
