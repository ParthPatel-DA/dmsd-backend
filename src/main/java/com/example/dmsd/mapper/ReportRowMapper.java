package com.example.dmsd.mapper;

import com.example.dmsd.model.Report;

import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportRowMapper implements RowMapper<Report> {

    @Override
    public Report mapRow(ResultSet rs, int rowNum) throws SQLException {
        String locationName = rs.getString("lname");
        String serviceName = rs.getString("sname");
        BigDecimal revenue = rs.getBigDecimal("revenue");

        return new Report(locationName, serviceName, revenue);
    }
}
