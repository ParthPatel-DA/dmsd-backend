package com.example.dmsd.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.dmsd.model.Location;
import org.springframework.jdbc.core.RowMapper;

public class LocationRowMapper implements RowMapper<Location> {

    @Override
    public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
        Location location = new Location();
        location.setManagerSsn(rs.getString("mssn"));
        location.setLocationId(rs.getInt("location_id"));
        location.setEmployeeSsn(rs.getString("essn"));
        location.setLocationName(rs.getString("lname"));
        location.setAddress(rs.getString("address"));
        return location;
    }

}
