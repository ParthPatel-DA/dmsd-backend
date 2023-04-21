package com.example.dmsd.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.dmsd.model.Vehicle;
import org.springframework.jdbc.core.RowMapper;

public class VehicleRowMapper implements RowMapper<Vehicle> {

    @Override
    public Vehicle mapRow(ResultSet rs, int rowNum) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(rs.getInt("vehicle_id"));
        vehicle.setCustomerId(rs.getInt("cid"));
        vehicle.setVehicleType(rs.getString("vtype"));
        vehicle.setVehicleYear(rs.getInt("vyear"));
        vehicle.setVehicleColor(rs.getString("color"));
        vehicle.setVehicleModel(rs.getString("vmodel"));
        vehicle.setVehicleManufacturer(rs.getString("manufacture"));
        return vehicle;
    }

}
