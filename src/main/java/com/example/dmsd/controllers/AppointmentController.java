package com.example.dmsd.controllers;

import com.example.dmsd.mapper.*;
import com.example.dmsd.model.*;
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
import java.time.LocalDate;
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


            String query = "\n" +
                    "SELECT SUM(retail_price) AS total_retail_price " +
                    "FROM PARTS " +
                    "WHERE part_id IN (" +
                    "  SELECT part_id" +
                    "  FROM SERVICES_PARTS" +
                    "  WHERE service_id = ?" +
                    ")";
            Map<String, Object> service2 = jdbcTemplate.queryForMap(query, appointment.getServiceId());

            BigDecimal totalRetailPrice = (BigDecimal) service2.get("total_retail_price");
            int totalPrice = totalRetailPrice.intValue();

            totalCharge += totalPrice;

            String sql3 = "INSERT INTO INVOICES (invoice_date, total_charge, Payment_method) VALUES (?, ?, 'PENDING')";
            int rows = jdbcTemplate.update(sql3, appointment.getAppointmentDate(), totalCharge);

            int invoiceId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

            String sql4 = "INSERT INTO APPOINTMENTS_SERVICES_INVOICES (service_id,appointment_id, invoice_id, price) VALUES (?, ?, ?, ?)";
            int rows1 = jdbcTemplate.update(sql4, appointment.getServiceId(), appointmentId, invoiceId, totalCharge);


            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Appointment created successfully"), HttpStatus.OK);


        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error creating Appointment." + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Update an existing appointment
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateAppointment(@PathVariable("id") int id, @RequestBody Appointment appointment) {
        try {
            String sql = "UPDATE appointments SET custid = ?, vechid = ?, locid = ?, techssn = ?, appointment_date = ?, status = ?, service_id = ? WHERE appointment_id = ?";
            int rows = jdbcTemplate.update(sql, appointment.getCustomerId(), appointment.getVehicleId(), appointment.getLocationId(), appointment.getTechnicianSsn(), appointment.getAppointmentDate(), appointment.getStatus(), appointment.getServiceId(), id);
            if (rows == 1) {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Appointment Updated successfully"), HttpStatus.OK);


            } else {
                throw new SQLException("Failed to update appointment");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error Updating Appointment." + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    // Update an existing appointment
    @PutMapping("/status/{id}")
    public ResponseEntity<CommonResponse> updateStatusAppointment(@PathVariable("id") int id, @RequestBody Appointment appointment) {
        try {
            String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
            int rows = jdbcTemplate.update(sql, appointment.getStatus(), id);
            if (rows == 1) {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Appointment Updated successfully"), HttpStatus.OK);
            } else {
                throw new SQLException("Failed to update appointment");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error Updating Appointment." + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

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
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error Deleting Appointment." + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Get a single appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getAppointment(@PathVariable("id") int id) {
        try {
            String sql = "SELECT a.*, l.lname, v.vtype FROM APPOINTMENTS a JOIN LOCATIONS l ON a.locid = l.location_id JOIN VEHICLE v ON a.vechid = v.vehicle_id WHERE a.appointment_id = ?";
            Appointment appointment = jdbcTemplate.queryForObject(sql, new AppointmentsRowMapper(), id);
            return new ResponseEntity<>(new CommonResponse(appointment, HttpStatus.OK.value(), "Appointment Fetched by id."), HttpStatus.OK);


        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "Failed finding Appointment." + e.getMessage()), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error Fetching Appointment by ID." + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Get a list of all appointments
    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getAllAppointments() {
        try {
            String sql = "SELECT a.*, l.lname, CONCAT(v.vtype, ' ', v.manufacture,' ',v.vmodel) AS vtype, p.firstname AS cname, i.total_charge, s.sname, i.Payment_method, i.invoice_date \n" +
                    "FROM APPOINTMENTS a " +
                    "JOIN LOCATIONS l ON a.locid = l.location_id " +
                    "JOIN VEHICLE v ON a.vechid = v.vehicle_id " +
                    "JOIN CUSTOMER c ON a.custid = c.person_id " +
                    "JOIN PERSON p ON c.person_id = p.person_id " +
                    "JOIN APPOINTMENTS_SERVICES_INVOICES asi ON a.appointment_id = asi.appointment_id " +
                    "JOIN INVOICES i ON asi.invoice_id = i.invoice_id JOIN SERVICES s ON asi.service_id = s.service_id ;";
            List<Appointment> appointments = jdbcTemplate.query(sql, new AppointmentsRowMapper());

            appointments.forEach(appointment -> {
                String sql1 = "SELECT s.* FROM APPOINTMENTS a " +
                        "JOIN APPOINTMENTS_SERVICES_INVOICES asi ON a.appointment_id = asi.appointment_id " +
                        "JOIN SERVICES s ON asi.service_id = s.service_id " +
                        "WHERE a.appointment_id = ?";

                Services foundService = jdbcTemplate.queryForObject(sql1, new Object[]{appointment.getAppointmentId()}, new ServicesRowMapper());

                String sql2 = "SELECT p.* FROM APPOINTMENTS a \n" +
                        "JOIN APPOINTMENTS_SERVICES_INVOICES asi ON a.appointment_id = asi.appointment_id \n" +
                        "JOIN SERVICES s ON asi.service_id = s.service_id \n" +
                        "JOIN SERVICES_PARTS sp ON sp.service_id = s.service_id \n" +
                        "JOIN PARTS p ON p.part_id = sp.part_id \n" +
                        "WHERE a.appointment_id = ?";

                List<Part> parts = jdbcTemplate.query(sql2, new Object[]{appointment.getAppointmentId()}, new PartsRowMapper());

                foundService.setPartList(parts);

                appointment.setServices(foundService);

            });

            return new ResponseEntity<>(new CommonResponse(appointments, HttpStatus.OK.value(), "All Appointments Fetched."), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching all Appointment." + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Get appointments by location ID
    @GetMapping("/loc/{locationId}")
    public ResponseEntity<CommonResponse> getAppointmentsByLocationId(@PathVariable("locationId") int locationId) {
        try {
            String sql = "SELECT a.*, l.lname, CONCAT(v.vtype, ' ', v.manufacture,' ',v.vmodel) AS vtype, p.firstname AS cname, i.total_charge, s.sname, i.Payment_method, i.invoice_date \n" +
                    "FROM APPOINTMENTS a " +
                    "JOIN LOCATIONS l ON a.locid = l.location_id " +
                    "JOIN VEHICLE v ON a.vechid = v.vehicle_id " +
                    "JOIN CUSTOMER c ON a.custid = c.person_id " +
                    "JOIN PERSON p ON c.person_id = p.person_id " +
                    "JOIN APPOINTMENTS_SERVICES_INVOICES asi ON a.appointment_id = asi.appointment_id " +
                    "JOIN INVOICES i ON asi.invoice_id = i.invoice_id JOIN SERVICES s ON asi.service_id = s.service_id  WHERE a.locid=?;";;
           // String sql = "SELECT a.*, v.vtype, l.lname FROM APPOINTMENTS a JOIN APPOINTMENTS_SERVICES_INVOICES asi ON a.appointment_id = asi.appointment_id JOIN INVOICES i ON asi.invoice_id = i.invoice_id JOIN VEHICLE v ON a.vechid = v.vehicle_id JOIN LOCATIONS l ON a.locid = l.location_id WHERE a.locid = ?";

            List<Appointment> appointments = jdbcTemplate.query(sql, new Object[]{locationId}, new AppointmentsRowMapper());

            appointments.forEach(appointment -> {
                String sql1 = "SELECT s.* FROM APPOINTMENTS a " +
                        "JOIN APPOINTMENTS_SERVICES_INVOICES asi ON a.appointment_id = asi.appointment_id " +
                        "JOIN SERVICES s ON asi.service_id = s.service_id " +
                        "WHERE a.appointment_id = ?";

                Services foundService = jdbcTemplate.queryForObject(sql1, new Object[]{appointment.getAppointmentId()}, new ServicesRowMapper());

                String sql2 = "SELECT p.* FROM APPOINTMENTS a \n" +
                        "JOIN APPOINTMENTS_SERVICES_INVOICES asi ON a.appointment_id = asi.appointment_id \n" +
                        "JOIN SERVICES s ON asi.service_id = s.service_id \n" +
                        "JOIN SERVICES_PARTS sp ON sp.service_id = s.service_id \n" +
                        "JOIN PARTS p ON p.part_id = sp.part_id \n" +
                        "WHERE a.appointment_id = ?";

                List<Part> parts = jdbcTemplate.query(sql2, new Object[]{appointment.getAppointmentId()}, new PartsRowMapper());

                foundService.setPartList(parts);

                appointment.setServices(foundService);

            });

            return new ResponseEntity<>(new CommonResponse(appointments, HttpStatus.OK.value(), "All Appointments Fetched by location ID."), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching all Appointment by location ID." + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Get appointments by customer ID
    @GetMapping("/cust/{customerId}")
    public ResponseEntity<CommonResponse> getAppointmentsByCustomerId(@PathVariable("customerId") int customerId) {
        try {
            String sql = "SELECT a.*, l.lname, CONCAT(v.vtype,' ',v.manufacture,' ',v.vmodel) AS vtype, p.firstname AS cname, i.total_charge, s.sname, i.Payment_method, i.invoice_date \n" +
                    "FROM APPOINTMENTS a " +
                    "JOIN LOCATIONS l ON a.locid = l.location_id " +
                    "JOIN VEHICLE v ON a.vechid = v.vehicle_id " +
                    "JOIN CUSTOMER c ON a.custid = c.person_id " +
                    "JOIN PERSON p ON c.person_id = p.person_id " +
                    "JOIN APPOINTMENTS_SERVICES_INVOICES asi ON a.appointment_id = asi.appointment_id " +
                    "JOIN INVOICES i ON asi.invoice_id = i.invoice_id JOIN SERVICES s ON asi.service_id = s.service_id WHERE a.custid=?;";
          //  String sql = "SELECT * FROM APPOINTMENTS natural join APPOINTMENTS_SERVICES_INVOICES natural join INVOICES WHERE custid = ?";
            List<Appointment> appointments = jdbcTemplate.query(sql, new Object[]{customerId}, new AppointmentsRowMapper());

            appointments.forEach(appointment -> {
                String sql1 = "SELECT s.* FROM APPOINTMENTS a " +
                        "JOIN APPOINTMENTS_SERVICES_INVOICES asi ON a.appointment_id = asi.appointment_id " +
                        "JOIN SERVICES s ON asi.service_id = s.service_id " +
                        "WHERE a.appointment_id = ?";

                Services foundService = jdbcTemplate.queryForObject(sql1, new Object[]{appointment.getAppointmentId()}, new ServicesRowMapper());

                String sql2 = "SELECT p.* FROM APPOINTMENTS a \n" +
                        "JOIN APPOINTMENTS_SERVICES_INVOICES asi ON a.appointment_id = asi.appointment_id \n" +
                        "JOIN SERVICES s ON asi.service_id = s.service_id \n" +
                        "JOIN SERVICES_PARTS sp ON sp.service_id = s.service_id \n" +
                        "JOIN PARTS p ON p.part_id = sp.part_id \n" +
                        "WHERE a.appointment_id = ?";

                List<Part> parts = jdbcTemplate.query(sql2, new Object[]{appointment.getAppointmentId()}, new PartsRowMapper());

                foundService.setPartList(parts);

                appointment.setServices(foundService);

            });

            return new ResponseEntity<>(new CommonResponse(appointments, HttpStatus.OK.value(), "All Appointments Fetched by Customer ID."), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching all Appointment by customer ID." + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PutMapping("/Upmcc/{appointmentId}")
    public ResponseEntity<CommonResponse> updateInvoicePaymentMethod(@PathVariable("appointmentId") int appointmentId, @RequestBody Appointment appointment) {
        try {
            // Update the payment_method in the Invoices table
            String updateInvoiceQuery = "UPDATE INVOICES SET Payment_method = ?, invoice_date = ? WHERE invoice_id = (SELECT invoice_id FROM APPOINTMENTS_SERVICES_INVOICES WHERE appointment_id = ?)";
            jdbcTemplate.update(updateInvoiceQuery,appointment.getPaymentMethod(), LocalDate.now(), appointmentId);

            // Update the credit_card attribute in the Customers table
            if (!appointment.getCreditCard().isEmpty()){
                String updateCustomerQuery = "UPDATE CUSTOMER SET credit_card = ? WHERE customer_id = (SELECT custid FROM APPOINTMENTS WHERE appointment_id = ?)";
                jdbcTemplate.update(updateCustomerQuery, appointment.getCreditCard(), appointmentId);
            }

            String updateStatusQuery = "UPDATE Appointments SET status = 'DONE' WHERE appointment_id = ?";
            jdbcTemplate.update(updateStatusQuery, appointmentId);

            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Payment Method and Credit card updated."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error updating payment method and credit card" + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}