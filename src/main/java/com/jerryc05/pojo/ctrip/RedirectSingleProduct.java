package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class RedirectSingleProduct {

    @JSONField(name = "flights")
    private List<FlightsItem> flights;

    public List<FlightsItem> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightsItem> flights) {
        this.flights = flights;
    }
}
