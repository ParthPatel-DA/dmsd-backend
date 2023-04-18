package com.example.dmsd.mapper;

import com.example.dmsd.model.Customer;
import com.example.dmsd.model.Person;
import com.example.dmsd.model.Person.PersonType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRowMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setId(rs.getLong("person_id"));
        person.setFirstName(rs.getString("firstName"));
        person.setAddress(rs.getString("address"));
        person.setEmail(rs.getString("email"));
        person.setPersonType(rs.getString("person_type"));
        person.setTelephone(rs.getString("telephone"));
        person.setToken(rs.getString("token"));

        Customer customer = new Customer(person, rs.getString("credit_card"));

        return customer;
    }
}
