package com.example.dmsd.controllers;

import com.example.dmsd.mapper.LocationRowMapper;
import com.example.dmsd.model.CommonResponse;
import com.example.dmsd.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/locations")
public class LocationController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Get all locations
    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getAllLocations() {
        try {
            List<Location> locations = jdbcTemplate.query("SELECT l.*, p.firstName AS manager_name FROM LOCATIONS l JOIN ( EMPLOYEE e NATURAL JOIN PERSON p ) ON l.mssn = e.ssn;", new LocationRowMapper());
            return new ResponseEntity<>(new CommonResponse(locations, HttpStatus.OK.value(), "All Locations Fetched Succesfully."), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch all Locations. " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Get a single location by ID
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getLocationById(@PathVariable("id") int id) {
        try {
            Location location = jdbcTemplate.queryForObject("SELECT * FROM LOCATIONS WHERE location_id = ?", new Object[]{id}, new LocationRowMapper());
            return new ResponseEntity<>(new CommonResponse(location, HttpStatus.OK.value(), "All Locations Fetched Succesfully by ID."), HttpStatus.OK);

        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch all Locations by ID. " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Create a new location
    @PostMapping("")
    public ResponseEntity<CommonResponse> createLocation(@RequestBody Location location) {
        try {
            int rows = jdbcTemplate.update("INSERT INTO LOCATIONS (mssn, location_id, essn, lname, address) VALUES (?, ?, ?, ?, ?)",
                    location.getManagerSsn(), location.getLocationId(), location.getEmployeeSsn(), location.getLocationName(), location.getAddress());
            if (rows == 1) {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.CREATED.value(), "Location created successfully"), HttpStatus.CREATED);

            } else {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to insert "), HttpStatus.INTERNAL_SERVER_ERROR);

            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR to insert. " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Update an existing location
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateLocation(@PathVariable("id") int id, @RequestBody Location location) {
        try {
            int rows = jdbcTemplate.update("UPDATE LOCATIONS SET mssn = ?, essn = ?, lname = ?, address = ? WHERE location_id = ?",
                    location.getManagerSsn(), location.getEmployeeSsn(), location.getLocationName(), location.getAddress(), id);
            if (rows == 1) {
                location.setLocationId(id);
                return new ResponseEntity<>(new CommonResponse(location, HttpStatus.OK.value(), "All Locations updated Succesfully by ID."), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "Failed to Find "), HttpStatus.NOT_FOUND);

            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR to find. " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Delete an existing location
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteLocation(@PathVariable("id") int id) {
        try {
            int rows = jdbcTemplate.update("DELETE FROM LOCATIONS WHERE location_id = ?", id);
            if (rows == 1) {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NO_CONTENT.value(), "Deleted Succesfully."), HttpStatus.NO_CONTENT);

            } else {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "Failed to Delete "), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR to Delete. " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}

