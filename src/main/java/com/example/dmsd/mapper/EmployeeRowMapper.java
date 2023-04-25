package com.example.dmsd.mapper;

import com.example.dmsd.model.Employee;
import com.example.dmsd.model.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setId(rs.getLong("person_id"));
        person.setFirstName(rs.getString("firstName"));
        person.setAddress(rs.getString("address"));
        person.setEmail(rs.getString("email"));
        person.setTelephone(rs.getString("telephone"));
        person.setPersonType(rs.getString("person_type"));

        Employee employee = new Employee(person);
        employee.setSsn(rs.getString("ssn"));
        employee.setSalary(rs.getLong("salary"));
        employee.setLocation_id(rs.getLong("location_id"));
        employee.setJob_type(rs.getString("job_type"));
        employee.setCommisionPercentage(rs.getDouble("commision_percentage"));
        employee.setExpertise(rs.getString("expertise"));
//        employee.setLocationName(rs.getString("lname"));

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            if (metaData.getColumnName(i).equals("lname")) {
                employee.setLocationName(rs.getString("lname"));
                break;
            }
        }

        return employee;
    }
}