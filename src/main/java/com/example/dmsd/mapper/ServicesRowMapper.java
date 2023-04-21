package com.example.dmsd.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.dmsd.model.Services;
import org.springframework.jdbc.core.RowMapper;

public class ServicesRowMapper implements RowMapper<Services> {

    @Override
    public Services mapRow(ResultSet rs, int rowNum) throws SQLException {
        Services services = new Services();
        services.setServiceId(rs.getInt("service_id"));
        services.setSkillId(rs.getInt("skillid"));
        services.setServiceName(rs.getString("sname"));
        services.setLaborPrice(rs.getInt("labor_price"));
        services.setAddCharge(rs.getInt("addcharge"));
        return services;
    }
}

