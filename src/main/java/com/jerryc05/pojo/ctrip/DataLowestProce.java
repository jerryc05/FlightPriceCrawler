package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

public class DataLowestProce {

    @JSONField(name = "oneWayPrice")
    private Map<String, Integer>[] oneWayPrice;

    @JSONField(name = "roundTripPrice")
    private Map<String, Map<String, Integer>> roundTripPrice;

    public Map<String, Integer>[] getOneWayPrice() {
        return oneWayPrice;
    }

    public void setOneWayPrice(Map<String, Integer>[] oneWayPrice) {
        this.oneWayPrice = oneWayPrice;
    }

    public Map<String, Map<String, Integer>> getRoundTripPrice() {
        return roundTripPrice;
    }

    public void setRoundTripPrice(Map<String, Map<String, Integer>> roundTripPrice) {
        this.roundTripPrice = roundTripPrice;
    }
}
