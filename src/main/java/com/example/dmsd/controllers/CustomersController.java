package com.example.dmsd.controllers;

import com.example.dmsd.mapper.CustomerRowMapper;
import com.example.dmsd.mapper.EmployeeRowMapper;
import com.example.dmsd.model.CommonResponse;
import com.example.dmsd.model.Customer;
import com.example.dmsd.model.Employee;
import com.example.dmsd.model.Person;
import com.example.dmsd.utils.JwtUtils;
import com.example.dmsd.utils.MyPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomersController {

    private final JdbcTemplate jdbcTemplate;
    private MyPasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;


    public CustomersController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<CommonResponse> registerUser(@RequestBody Person person) {
        String checkQuery = "SELECT * FROM person WHERE email=?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(checkQuery, person.getEmail());
        if (!rows.isEmpty()) {
            // Email already exists, return a 409 conflict response
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.CONFLICT.value(), "User with email already exists"), HttpStatus.CONFLICT);
        }
        String query = "INSERT INTO dmsd.PERSON\n" +
                "(pass, firstName, address, email, telephone, person_type)\n" +
                "VALUES(?, ?, ?, ?, ?, ?);";

        String query1 = "INSERT INTO dmsd.CUSTOMER\n" +
                "(person_id, credit_card)\n" +
                "VALUES(?, ?);";

        // Create a new KeyHolder instance to store the generated ID
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            // Execute the insert statement and retrieve the generated ID
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, new String[]{"person_id"});
                ps.setString(1, person.getPass());
                ps.setString(2, person.getFirstName());
                ps.setString(3, person.getAddress());
                ps.setString(4, person.getEmail());
                ps.setString(5, person.getTelephone());
                ps.setObject(6, Person.PersonType.CUSTOMER.ordinal());
                return ps;
            }, keyHolder);

            // Retrieve the generated ID from the KeyHolder
            int personId = keyHolder.getKey().intValue();

            // Execute the second insert statement using the generated ID
            jdbcTemplate.update(query1, personId, "");

            String query2 = "SELECT * FROM PERSON p natural join CUSTOMER c WHERE p.email = ? AND p.pass = ?";

            Customer foundCust = jdbcTemplate.queryForObject(query2, new Object[]{person.getEmail(), person.getPass()}, new CustomerRowMapper());
            String token = jwtUtils.generateToken(foundCust.getPerson().getEmail());

            if (foundCust != null) {
                foundCust.getPerson().setToken(token);
                return new ResponseEntity<>(new CommonResponse(foundCust, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "Invalid username or password"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to login user"+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Login a user
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> loginUser(@RequestBody Person person) {
        String query = "SELECT * FROM PERSON p WHERE p.email = ? AND p.pass = ?";
        try {
            Person foundPerson = jdbcTemplate.queryForObject(query, new Object[]{person.getEmail(), person.getPass()}, new PersonMapper());
            String token = jwtUtils.generateToken(foundPerson.getEmail());
            if(foundPerson != null) {
//                Map<String, Object> obj = new HashMap<>();
//                obj.put("person", person);

                if(foundPerson.getPersonType() == Person.PersonType.ADMIN) {
                    foundPerson.setToken(token);
                    return new ResponseEntity<>(new CommonResponse(foundPerson, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
                } else if(foundPerson.getPersonType() == Person.PersonType.CUSTOMER) {
                    String query1 = "SELECT * FROM PERSON p natural join CUSTOMER c WHERE p.person_id = ?";

                    Customer foundCust = jdbcTemplate.queryForObject(query1, new Object[]{foundPerson.getId()}, new CustomerRowMapper());
                    foundCust.getPerson().setToken(token);

                    return new ResponseEntity<>(new CommonResponse(foundCust, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
                } else if(foundPerson.getPersonType() == Person.PersonType.EMPLOYEE) {
                    String query1 = "SELECT * FROM PERSON p natural join Employee e WHERE p.person_id = ?";
                    Employee foundEmp = jdbcTemplate.queryForObject(query1, new Object[]{foundPerson.getId()}, new EmployeeRowMapper());
                    foundEmp.getPerson().setToken(token);

                    if(foundEmp.getJob_type() == Employee.JobType.MANAGER) {
                        return new ResponseEntity<>(new CommonResponse(foundEmp, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "You are not having access."), HttpStatus.NOT_FOUND);
                    }
                } else {
                    return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "You are not having access."), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "Invalid username or password"), HttpStatus.NOT_FOUND);
            }
//            if (foundPerson != null) {
//                foundPerson.getPerson().setToken(token);
//                return new ResponseEntity<>(new CommonResponse(foundCust, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "Invalid username or password"), HttpStatus.NOT_FOUND);
//            }
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "Invalid username or password"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to login user"+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
//        String query = "SELECT * FROM PERSON p natural join CUSTOMER c WHERE p.email = ? AND p.pass = ?";
//        try {
//            Customer foundCust = jdbcTemplate.queryForObject(query, new Object[]{person.getEmail(), person.getPass()}, new CustomerRowMapper());
//            String token = jwtUtils.generateToken(foundCust.getPerson().getEmail());
//
//            if (foundCust != null) {
//                foundCust.getPerson().setToken(token);
//                return new ResponseEntity<>(new CommonResponse(foundCust, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "Invalid username or password"), HttpStatus.NOT_FOUND);
//            }
//        } catch (EmptyResultDataAccessException e) {
//            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.NOT_FOUND.value(), "Invalid username or password"), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to login user"+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getPerson(@PathVariable("id") Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
//        if(token == null || token.isEmpty()) {
//            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.FORBIDDEN.value(), "Token not provided"), HttpStatus.OK);
//        } else if(!jwtUtils.validateToken(token)) {
//            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.UNAUTHORIZED.value(), "Token expired"), HttpStatus.OK);
//        }
//
//        String emil = jwtUtils.getEmailFromToken(token);
//        System.out.println(emil);

        String query = "SELECT * FROM PERSON p natural join CUSTOMER c WHERE p.person_id = ?";
        try {
            Customer foundCust = jdbcTemplate.queryForObject(query, new Object[]{id}, new CustomerRowMapper());
            if (foundCust != null) {
                return new ResponseEntity<>(new CommonResponse(foundCust, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to login user"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // all
    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getAllCustomers(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String query = "SELECT * FROM PERSON p natural join CUSTOMER c";
        try {
            List<Customer> customers = jdbcTemplate.query(query, new CustomerRowMapper());
            return new ResponseEntity<>(new CommonResponse(customers, HttpStatus.OK.value(), "Success"), HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve customers"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update a user
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateUser(@PathVariable("id") Long id, @RequestBody Person person) {
        String query = "UPDATE person SET firstName = ?, telephone = ?, email = ?, address = ? WHERE person_id = ?";
        try {
            int result = jdbcTemplate.update(query, person.getFirstName(), person.getTelephone(), person.getEmail(), person.getAddress(), id);
            if (result > 0) {
                String query1 = "SELECT * FROM PERSON p natural join CUSTOMER c WHERE p.person_id = ?";
                Customer foundCust = jdbcTemplate.queryForObject(query1, new Object[]{id}, new CustomerRowMapper());
                String token = jwtUtils.generateToken(foundCust.getPerson().getEmail());
                foundCust.getPerson().setToken(token);

                return new ResponseEntity<>(new CommonResponse(foundCust, HttpStatus.OK.value(), "User updated successfully"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponse(result, HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update user"+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//
//    // Delete a user
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
//        String query = "DELETE FROM users WHERE id = ?";
//        try {
//            int result = jdbcTemplate.update(query, id);
//            if (result > 0) {
//                return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>("Failed to delete user", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



    // UserMapper class to map query results to User object
    private static class PersonMapper implements RowMapper<Person> {

        @Override
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setId(rs.getLong("person_id"));
            person.setFirstName(rs.getString("firstName"));
            person.setAddress(rs.getString("address"));
            person.setEmail(rs.getString("email"));
            person.setPersonType(rs.getString("person_type"));
            person.setTelephone(rs.getString("telephone"));
            return person;
        }
    }

}