package com.example.dmsd.controllers;

import com.example.dmsd.mapper.AppointmentsRowMapper;
import com.example.dmsd.mapper.ServicesRowMapper;
import com.example.dmsd.mapper.VehicleRowMapper;
import com.example.dmsd.model.Appointment;
import com.example.dmsd.model.CommonResponse;
import com.example.dmsd.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Service;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
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
//            int rows = jdbcTemplate.update(sql, appointment.getCustomerId(), appointment.getVehicleId(), appointment.getLocationId(), appointment.getTechnicianSsn(), appointment.getAppointmentDate(), appointment.getStatus());
//            if (rows == 1) {
//                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.CREATED.value(), "Appointment created successfully"), HttpStatus.OK);
//
//            } else {
//                throw new SQLException("Failed to create appointment");
//            }
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, appointment.getCustomerId());
                statement.setInt(2, appointment.getVehicleId());
                statement.setInt(3, appointment.getLocationId());
                statement.setString(4, appointment.getTechnicianSsn());
                statement.setDate(5, Date.valueOf(appointment.getAppointmentDate()));
                statement.setString(6, appointment.getStatus());
                return statement;
            }, keyHolder);
            int appointmentId = keyHolder.getKey().intValue();


            String sql1 = "SELECT labor_price, addcharge FROM services WHERE service_id = ?";
            Map<String, Object> service1 = jdbcTemplate.queryForMap(sql1, appointment.getServiceId());
            int laborPrice = (int) service1.get("labor_price");
            int addCharge = (int) service1.get("addcharge");

            // Calculate total charge
            int totalCharge = laborPrice + addCharge;


            String query = "SELECT SUM(retail_price) as total_retail_price FROM Parts WHERE servid = ?";
            Map<String, Object> service2 = jdbcTemplate.queryForMap(query, appointment.getServiceId());

            BigDecimal totalRetailPrice = (BigDecimal) service2.get("total_retail_price");
            int totalPrice = totalRetailPrice.intValue();

            totalCharge += totalPrice;

            String sql3 = "INSERT INTO INVOICES (invoice_date, total_charge, Payment_method) VALUES (?, ?, 'PENDING')";
            int rows = jdbcTemplate.update(sql3, appointment.getAppointmentDate(), totalCharge);

            int invoiceId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

            String sql4 = "INSERT INTO APPOINTMENTS_SERVICES_INVOICES (service_id,appointment_id, invoice_id, price) VALUES (?, ?, ?, ?)";
            int rows1 = jdbcTemplate.update(sql4,appointment.getServiceId(), appointmentId,invoiceId, totalCharge);


            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Appointment created successfully"), HttpStatus.OK);





        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error creating Appointment." + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Update an existing appointment
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateAppointment(@PathVariable("id") int id, @RequestBody Appointment appointment) {
        try {
            String sql =  "UPDATE appointments SET custid = ?, vechid = ?, locid = ?, techssn = ?, appointment_date = ?, status = ?, service_id = ? WHERE appointment_id = ?";
            int rows = jdbcTemplate.update(sql, appointment.getCustomerId(), appointment.getVehicleId(), appointment.getLocationId(), appointment.getTechnicianSsn(), appointment.getAppointmentDate(), appointment.getStatus(),appointment.getServiceId(), id);
            if (rows == 1) {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Appointment Updated successfully"), HttpStatus.OK);


            } else {
                throw new SQLException("Failed to update appointment");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error Updating Appointment."+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    // Update an existing appointment
    @PutMapping("/status/{id}")
    public ResponseEntity<CommonResponse> updateStatusAppointment(@PathVariable("id") int id, @RequestBody Appointment appointment) {
        try {
            String sql =  "UPDATE appointments SET status = ? WHERE appointment_id = ?";
            int rows = jdbcTemplate.update(sql, appointment.getStatus(), id);
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
            String sql = "SELECT * FROM APPOINTMENTS natural join APPOINTMENTS_SERVICES_INVOICES natural join INVOICES";
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
            String sql = "SELECT * FROM APPOINTMENTS natural join APPOINTMENTS_SERVICES_INVOICES natural join INVOICES WHERE locid = ?";
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
            String sql = "SELECT * FROM APPOINTMENTS natural join APPOINTMENTS_SERVICES_INVOICES natural join INVOICES WHERE custid = ?";
            List<Appointment> appointments = jdbcTemplate.query(sql, new Object[]{customerId}, new AppointmentsRowMapper());
            return new ResponseEntity<>(new CommonResponse(appointments, HttpStatus.OK.value(), "All Appointments Fetched by Customer ID."), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching all Appointment by customer ID."+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
