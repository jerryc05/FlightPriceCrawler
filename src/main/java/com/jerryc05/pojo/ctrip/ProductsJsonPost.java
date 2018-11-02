package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

public class ProductsJsonPost {

    @JSONField(name = "flightWay")
    private String flightWay = "Oneway";

    @JSONField(name = "searchIndex")
    private int searchIndex = 1;

    @JSONField(name = "airportParams")
    private AirportParamsItem[] airportParams;

    public AirportParamsItem[] getAirportParams() {
        return airportParams;
    }

    public void setAirportParams(AirportParamsItem[] airportParams) {
        this.airportParams = airportParams;
    }

    public int getSearchIndex() {
        return searchIndex;
    }

    public void setSearchIndex(int searchIndex) {
        this.searchIndex = searchIndex;
    }

    public String getFlightWay() {
        return flightWay;
    }

    public void setFlightWay(String flightWay) {
        this.flightWay = flightWay;
    }
}