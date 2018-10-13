package com.jerryc05.pojo.ctrip;


import com.alibaba.fastjson.annotation.JSONField;

public class Data {

    @JSONField(name = "recommendData")
    private RecommendData recommendData;

    @JSONField(name = "routeList")
    private RouteListItem[] routeList;

    public void setRecommendData(RecommendData recommendData) {
        this.recommendData = recommendData;
    }

    public void setRouteList(RouteListItem[] routeList) {
        this.routeList = routeList;
    }

    public RecommendData getRecommendData() {
        return recommendData;
    }

    public RouteListItem[] getRouteList() {
        return routeList;
    }
}