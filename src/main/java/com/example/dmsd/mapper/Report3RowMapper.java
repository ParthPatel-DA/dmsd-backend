package com.example.dmsd.mapper;




import com.example.dmsd.model.Report;
import com.example.dmsd.model.Report3;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.String;
import java.lang.Double;

public class Report3RowMapper implements RowMapper<Report3> {

    @Override
    public Report3 mapRow(ResultSet rs, int rowNum) throws SQLException {
        Report3 location2 = new Report3();
        location2.setLocationName(rs.getString("lname"));
        location2.setRevenue(rs.getDouble("revenue"));

        return location2;
    }

}
