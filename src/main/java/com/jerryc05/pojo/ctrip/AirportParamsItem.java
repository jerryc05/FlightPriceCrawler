package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

public class AirportParamsItem {

    @JSONField(name = "dcity")
    private String dcity;

    @JSONField(name = "acity")
    private String acity;

    @JSONField(name = "date")
    private String date;

    public String getDcity() {
        return dcity;
    }

    public String getDate() {
        return date;
    }

    public String getAcity() {
        return acity;
    }

    public void setDcity(String dcity) {
        this.dcity = dcity;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAcity(String acity) {
        this.acity = acity;
    }
}