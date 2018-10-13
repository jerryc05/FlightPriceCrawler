package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

public class RouteListItem implements Comparable<RouteListItem> {

    @JSONField(name = "legs")
    private LegsItem[] legs;

    @JSONField(name = "routeType")
    private String routeType;

    @JSONField(name = "combinedPrice")
    private int combinedPrice;

    public int getCombinedPrice() {
        return combinedPrice;
    }

    public void setCombinedPrice(int combinedPrice) {
        this.combinedPrice = combinedPrice;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public LegsItem[] getLegs() {
        return legs;
    }

    public void setLegs(LegsItem[] legs) {
        this.legs = legs;
    }

    @Override
    public int compareTo(RouteListItem o) {
        return this.getLegs()[0].getCharacteristic().getLowestPrice()
                - o.getLegs()[0].getCharacteristic().getLowestPrice();
    }
}
