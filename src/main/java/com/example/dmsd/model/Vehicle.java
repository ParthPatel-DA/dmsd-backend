package com.example.dmsd.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Vehicle {
    private int vehicleId;
    private int customerId;
    private String vehicleType;
    private int vehicleYear;
    private String vehicleColor;
    private String vehicleModel;
    private String vehicleManufacturer;

    public Vehicle(int vehicleId, int customerId, String vehicleType, int vehicleYear,
                   String vehicleColor, String vehicleModel, String vehicleManufacturer) {
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.vehicleType = vehicleType;
        this.vehicleYear = vehicleYear;
        this.vehicleColor = vehicleColor;
        this.vehicleModel = vehicleModel;
        this.vehicleManufacturer = vehicleManufacturer;
    }

    public Vehicle() {

    }

    public static Vehicle fromResultSet(ResultSet rs) throws SQLException {
        int vehicleId = rs.getInt("vehicle_id");
        int customerId = rs.getInt("cid");
        String vehicleType = rs.getString("vtype");
        int vehicleYear = rs.getInt("vyear");
        String vehicleColor = rs.getString("color");
        String vehicleModel = rs.getString("vmodel");
        String vehicleManufacturer = rs.getString("manufacture");
        return new Vehicle(vehicleId, customerId, vehicleType, vehicleYear,
                vehicleColor, vehicleModel, vehicleManufacturer);
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(int vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleManufacturer() {
        return vehicleManufacturer;
    }

    public void setVehicleManufacturer(String vehicleManufacturer) {
        this.vehicleManufacturer = vehicleManufacturer;
    }
}