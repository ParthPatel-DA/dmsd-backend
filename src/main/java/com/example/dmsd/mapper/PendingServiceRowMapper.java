package com.example.dmsd.mapper;

import com.example.dmsd.model.PendingService;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PendingServiceRowMapper implements RowMapper<PendingService> {
    @Override
    public PendingService mapRow(ResultSet rs, int rowNum) throws SQLException {
        PendingService service = new PendingService();
        service.setPending_count(rs.getDouble("pending_count"));
        service.setLocationName(rs.getString("lname"));
        service.setServiceName(rs.getString("sname"));
        return service;
    }
}
