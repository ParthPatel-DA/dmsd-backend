package com.example.dmsd.controllers;

import com.example.dmsd.mapper.PartsRowMapper;
import com.example.dmsd.model.CommonResponse;
import com.example.dmsd.model.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
public class PartsController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Create a new part
    @PostMapping("")
    public ResponseEntity<CommonResponse> createPart(@RequestBody Part part) {
        String query = "INSERT INTO PARTS (quantity, status, pname, retail_price) VALUES (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(query, part.getQuantity(), part.getStatus(), part.getPname(), part.getRetailPrice());
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Part created successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to create part"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an existing part by ID
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updatePart(@PathVariable("id") int id, @RequestBody Part part) {
        String query = "UPDATE PARTS SET servid = ?, quantity = ?, status = ?, pname = ?, retail_price = ? WHERE part_id = ?";
        try {
            jdbcTemplate.update(query, part.getQuantity(), part.getStatus(), part.getPname(), part.getRetailPrice(), id);
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Part updated successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update part"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a part by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deletePart(@PathVariable("id") int id) {
        String query = "DELETE FROM PARTS WHERE part_id = ?";
        try {
            jdbcTemplate.update(query, id);
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.OK.value(), "Part deleted successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete part"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a part by ID
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getPartById(@PathVariable("id") int id) {
        String query = "SELECT * FROM PARTS WHERE part_id = ?";
        try {
            Part part = jdbcTemplate.queryForObject(query, new Object[]{id}, new PartsRowMapper());
            if (part != null) {
                return new ResponseEntity<>(new CommonResponse(part, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to get part"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all parts
    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getAllParts() {
        try {
            String query = "SELECT * FROM PARTS";
            List<Part> parts = jdbcTemplate.query(query, new PartsRowMapper());
            return new ResponseEntity<>(new CommonResponse(parts, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve parts"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}