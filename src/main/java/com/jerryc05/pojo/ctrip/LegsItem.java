package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class LegsItem {

    @JSONField(name = "flight")
    private Flight flight;

    @JSONField(name = "characteristic")
    private Characteristic characteristic;

    @JSONField(name = "cabins")
    private List<CabinsItem> cabins;

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }

    public List<CabinsItem> getCabins() {
        return cabins;
    }

    public void setCabins(List<CabinsItem> cabins) {
        this.cabins = cabins;
    }
}
