package com.example.dmsd.model;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Services {

    private int serviceId;
    private int skillId;
    private String serviceName;
    private int laborPrice;
    private int addCharge;
    private String partsName;

    private List<Integer> partIds;

    private List<Part> partList;

    public Services() {}

    public Services(int serviceId, int skillId, String serviceName, int laborPrice, int addCharge, String partsName, List<Integer> partIds, List<Part> partList) {
        this.serviceId = serviceId;
        this.skillId = skillId;
        this.serviceName = serviceName;
        this.laborPrice = laborPrice;
        this.addCharge = addCharge;
        this.partsName = partsName;
        this.partIds = partIds;
        this.partList = partList;
    }

    public List<Part> getPartList() {
        return partList;
    }

    public void setPartList(List<Part> partList) {
        this.partList = partList;
    }

    public String getPartsName() {
        return partsName;
    }

    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }

    public List<Integer> getPartIds() {
        return partIds;
    }

    public void setPartIds(List<Integer> partIds) {
        this.partIds = partIds;
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
