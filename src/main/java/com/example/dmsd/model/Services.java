package com.example.dmsd.model;

public class Services {
    private Long service_id;
    private Long skillid;
    private String sname;
    private Double labor_price;
    private Double addcharge;

    public Services() {
    }

    public Services(Long service_id, Long skillid, String sname, Double labor_price, Double addcharge) {
        this.service_id = service_id;
        this.skillid = skillid;
        this.sname = sname;
        this.labor_price = labor_price;
        this.addcharge = addcharge;
    }

    public Long getService_id() {
        return service_id;
    }

    public void setService_id(Long service_id) {
        this.service_id = service_id;
    }

    public Long getSkillid() {
        return skillid;
    }

    public void setSkillid(Long skillid) {
        this.skillid = skillid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Double getLabor_price() {
        return labor_price;
    }

    public void setLabor_price(Double labor_price) {
        this.labor_price = labor_price;
    }

    public Double getAddcharge() {
        return addcharge;
    }

    public void setAddcharge(Double addcharge) {
        this.addcharge = addcharge;
    }
}
