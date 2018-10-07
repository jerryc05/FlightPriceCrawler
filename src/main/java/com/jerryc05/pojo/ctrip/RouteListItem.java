package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class RouteListItem {

    @JSONField(name = "legs")
    private List<LegsItem> legs;

    public List<LegsItem> getLegs() {
        return legs;
    }

    public void setLegs(List<LegsItem> legs) {
        this.legs = legs;
    }
}
