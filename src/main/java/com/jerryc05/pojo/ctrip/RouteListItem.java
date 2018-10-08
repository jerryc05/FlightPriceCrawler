package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class RouteListItem implements Comparable<RouteListItem> {

    @JSONField(name = "legs")
    private List<LegsItem> legs;

    public List<LegsItem> getLegs() {
        return legs;
    }

    public void setLegs(List<LegsItem> legs) {
        this.legs = legs;
    }

    @Override
    public int compareTo(RouteListItem o) {
        return this.getLegs().get(0).getCharacteristic().getLowestPrice()
                - o.getLegs().get(0).getCharacteristic().getLowestPrice();
    }
}
