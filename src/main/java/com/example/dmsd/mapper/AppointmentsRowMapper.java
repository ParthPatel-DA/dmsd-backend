package com.example.dmsd.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.dmsd.model.Appointment;
import org.springframework.jdbc.core.RowMapper;

public class AppointmentsRowMapper implements RowMapper<Appointment> {

    @Override
    public Appointment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setCustomerId(rs.getInt("custid"));
        appointment.setVehicleId(rs.getInt("vechid"));
        appointment.setLocationId(rs.getInt("locid"));
        appointment.setTechnicianSsn(rs.getString("techssn"));
        appointment.setAppointmentDate(rs.getDate("appointment_date").toLocalDate());
        appointment.setStatus(rs.getString("status"));
        appointment.setTotal_charge(rs.getDouble("total_charge"));
//        appointment.setServiceId(rs.getInt("service_id"));
        return appointment;
    }

}