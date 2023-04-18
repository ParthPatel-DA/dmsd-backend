package com.example.dmsd.model;

public class CommonResponse {
    private Object data;
    private Integer status;
    private String msg;

    public CommonResponse(Object data, Integer status, String msg) {
        this.data = data;
        this.status = status;
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
