package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

public class RedirectSingleProduct {

    @JSONField(name = "flights")
    private FlightsItem[] flights;

    public FlightsItem[] getFlights() {
        return flights;
    }

    public void setFlights(FlightsItem[] flights) {
        this.flights = flights;
    }
}
