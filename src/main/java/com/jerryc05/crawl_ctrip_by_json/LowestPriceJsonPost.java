package com.jerryc05.crawl_ctrip_by_json;

import com.alibaba.fastjson.annotation.JSONField;

public class LowestPriceJsonPost {

    @JSONField(name = "flightWay")
    String flightWay;

    @JSONField(name = "dcity")
    String dcity;

    @JSONField(name = "acity")
    String acity;

    @JSONField(name = "army")
    boolean army = false;

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