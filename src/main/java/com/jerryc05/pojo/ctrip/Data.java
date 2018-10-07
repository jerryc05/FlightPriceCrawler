package com.jerryc05.pojo.ctrip;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class Data {

    @JSONField(name = "recommendData")
    private RecommendData recommendData;

    @JSONField(name = "routeList")
    private List<RouteListItem> routeList;

    public void setRecommendData(RecommendData recommendData) {
        this.recommendData = recommendData;
    }

    public void setRouteList(List<RouteListItem> routeList) {
        this.routeList = routeList;
    }

    public RecommendData getRecommendData() {
        return recommendData;
    }

    public List<RouteListItem> getRouteList() {
        return routeList;
    }
}