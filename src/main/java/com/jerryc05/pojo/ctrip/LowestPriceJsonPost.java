package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

@SuppressWarnings("unused")
public class LowestPriceJsonPost {

    @JSONField(name = "flightWay")
    private String flightWay;

    @JSONField(name = "dcity")
    private String dcity;

    @JSONField(name = "acity")
    private String acity;

    @JSONField(name = "army")
    private boolean army = false;

    public String getDcity() {
        return dcity;
    }

    public String getAcity() {
        return acity;
    }

    public String getFlightWay() {
        return flightWay;
    }

    public void setDcity(String dcity) {
        this.dcity = dcity;
    }

    public void setAcity(String acity) {
        this.acity = acity;
    }

    public void setFlightWay(String flightWay) {
        this.flightWay = flightWay;
    }

    public boolean isArmy() {
        return army;
    }

    public void setArmy(boolean army) {
        this.army = army;
    }
}