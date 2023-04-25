package com.example.dmsd.mapper;

import com.example.dmsd.model.Report;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ReportRowMapper implements RowMapper<Report> {

    @Override
    public Report mapRow(ResultSet rs, int rowNum) throws SQLException {
        Report report = new Report();
        String locationName = rs.getString("lname");
        String serviceName = rs.getString("sname");
        String revenue = rs.getString("revenue");
        return  report;
    }
//    public Report mapRevenueByLocationRow(ResultSet resultSet) throws SQLException {
//        Report report1 = new Report();
//        String locationName = resultSet.getString("lname");
//        String revenue = resultSet.getString("revenue");
//
//        return report1;
//    }


    }




