package com.example.dmsd.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.example.dmsd.model.Services;
import org.springframework.jdbc.core.RowMapper;

public class ServicesRowMapper implements RowMapper<Services> {

    @Override
    public Services mapRow(ResultSet rs, int rowNum) throws SQLException {
        Services services = new Services();
        services.setServiceId(rs.getInt("service_id"));
//        services.setSkillId(rs.getInt("skillid"));
        services.setServiceName(rs.getString("sname"));
        services.setLaborPrice(rs.getInt("labor_price"));
        services.setAddCharge(rs.getInt("addcharge"));
//        services.setPartsName(rs.getString("parts_name"));

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            if (metaData.getColumnName(i).equals("parts_name")) {
                services.setPartsName(rs.getString("parts_name"));
                break;
            }
        }
        return services;
    }
}

