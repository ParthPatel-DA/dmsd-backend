package com.example.dmsd.controllers;

import com.example.dmsd.mapper.VehicleRowMapper;
import com.example.dmsd.model.CommonResponse;
import com.example.dmsd.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Create a new vehicle
    @PostMapping(" ")
    public ResponseEntity<CommonResponse> createVehicle(@RequestBody Vehicle vehicle) {
        try {
            String sql = "INSERT INTO VEHICLE (cid, vtype, vyear, color, vmodel, manufacture) VALUES (?, ?, ?, ?, ?, ?)";
            int rows = jdbcTemplate.update(sql, vehicle.getCustomerId(), vehicle.getVehicleType(), vehicle.getVehicleYear(),
                    vehicle.getVehicleColor(), vehicle.getVehicleModel(), vehicle.getVehicleManufacturer());
            if (rows > 0) {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Vehicle created successfully"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to create vehicle"), HttpStatus.INTERNAL_SERVER_ERROR);

            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error to create vehicle"+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // Update an existing vehicle
    @PutMapping("/{vehicleId}")
    public ResponseEntity<CommonResponse> updateVehicle(@PathVariable("vehicleId") int vehicleId, @RequestBody Vehicle vehicle) {
        try {
            String sql = "UPDATE VEHICLE SET cid=?, vtype=?, vyear=?, color=?, vmodel=?, manufacture=? WHERE vehicle_id=?";
            int rows = jdbcTemplate.update(sql, vehicle.getCustomerId(), vehicle.getVehicleType(), vehicle.getVehicleYear(),
                    vehicle.getVehicleColor(), vehicle.getVehicleModel(), vehicle.getVehicleManufacturer(), vehicleId);
            if (rows > 0) {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Vehicle updated successfully"), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update vehicle"), HttpStatus.INTERNAL_SERVER_ERROR);

            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching data."+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete an existing vehicle
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<CommonResponse> deleteVehicle(@PathVariable("vehicleId") int vehicleId) {
            try {
                String sql = "DELETE FROM VEHICLE WHERE vehicle_id=?";
                int rows = jdbcTemplate.update(sql, vehicleId);
                if (rows > 0) {
                    return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Vehicle deleted successfully"), HttpStatus.OK);

                } else {
                    return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete vehicle"), HttpStatus.INTERNAL_SERVER_ERROR);

                }
            } catch (Exception e) {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error deleting data."+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    // Get a vehicle by ID
    @GetMapping("/{vehicleId}")
    public ResponseEntity<CommonResponse> getVehicleById(@PathVariable("vehicleId") int vehicleId) {
        try {
            String sql = "SELECT * FROM VEHICLE WHERE vehicle_id=?";
            Vehicle vehicle = jdbcTemplate.queryForObject(sql, new Object[]{vehicleId}, new VehicleRowMapper());
            if (vehicle != null) {
                return new ResponseEntity<>(new CommonResponse(vehicle, HttpStatus.OK.value(), "Vehicle fetched."), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "Failed to fetch."), HttpStatus.NOT_FOUND);

            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching data."+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a vehicle by user id
    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse> getVehicleByUserId(@PathVariable("userId") int userId) {
        try {
            String sql = "SELECT * FROM VEHICLE WHERE cid=?";
//            List<Vehicle> vehicles = jdbcTemplate.queryForObject(sql, new Object[]{userId}, new VehicleRowMapper());
            List<Vehicle> vehicles = jdbcTemplate.query(sql, new VehicleRowMapper(), new Object[]{userId});
            return new ResponseEntity<>(new CommonResponse(vehicles, HttpStatus.OK.value(), "Vehicle fetched."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching data."+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all vehicles
    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getAllVehicles() {
        try {
            String sql = "SELECT * FROM VEHICLE";
            List<Vehicle> vehicles = jdbcTemplate.query(sql, new VehicleRowMapper());
            return new ResponseEntity<>(new CommonResponse(vehicles, HttpStatus.OK.value(), "Vehicle fetched."), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching data."+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
