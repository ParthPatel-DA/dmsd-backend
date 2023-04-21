package com.example.dmsd.model;


import java.sql.ResultSet;
import java.sql.SQLException;

public class Services {

    private int serviceId;
    private int skillId;
    private String serviceName;
    private int laborPrice;
    private int addCharge;

    public Services() {}

    public Services(int serviceId, int skillId, String serviceName, int laborPrice, int addCharge) {
        this.serviceId = serviceId;
        this.skillId = skillId;
        this.serviceName = serviceName;
        this.laborPrice = laborPrice;
        this.addCharge = addCharge;
    }

    public Services(ResultSet rs) throws SQLException {
        this.serviceId = rs.getInt("service_id");
        this.skillId = rs.getInt("skillid");
        this.serviceName = rs.getString("sname");
        this.laborPrice = rs.getInt("labor_price");
        this.addCharge = rs.getInt("addcharge");
    }

    // Getters and Setters
    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getLaborPrice() {
        return laborPrice;
    }

    public void setLaborPrice(int laborPrice) {
        this.laborPrice = laborPrice;
    }

    public int getAddCharge() {
        return addCharge;
    }

    public void setAddCharge(int addCharge) {
        this.addCharge = addCharge;
    }
}
