package com.example.dmsd.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.dmsd.model.Part;
import org.springframework.jdbc.core.RowMapper;

public class PartsRowMapper implements RowMapper<Part> {

    @Override
    public Part mapRow(ResultSet rs, int rowNum) throws SQLException {
        Part part = new Part();
        part.setPartId(rs.getInt("part_id"));
//        part.setServiceId(rs.getInt("servid"));
        part.setQuantity(rs.getInt("quantity"));
        part.setStatus(rs.getString("status"));
        part.setPname(rs.getString("pname"));
        part.setRetailPrice(rs.getInt("retail_price"));
        return part;
    }

}
