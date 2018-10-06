package com.jerryc05.crawl_ctrip_by_json;

import com.alibaba.fastjson.annotation.JSONField;

class LowestPriceJsonPost {

    @JSONField(name = "dcity")
    String dcity;

    @JSONField(name = "acity")
    String acity;

    @JSONField(name = "flightWay")
    String flightWay;

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
}