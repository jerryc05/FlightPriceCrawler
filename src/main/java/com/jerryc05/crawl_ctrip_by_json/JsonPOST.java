package com.jerryc05.crawl_ctrip_by_json;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

@SuppressWarnings("unused")
class JsonPOST {

    @JSONField(name = "airportParams")
    List<AirportParamsItem> airportParams;

    @JSONField(name = "hasBaby")
    boolean hasBaby = false;

    @JSONField(name = "hasChild")
    boolean hasChild = false;

    @JSONField(name = "searchIndex")
    int searchIndex = 1;

    @JSONField(name = "flightWay")
    String flightWay = "Oneway";

    @JSONField(name = "classType")
    String classType = "ALL";

    @SuppressWarnings("unused")
    static class AirportParamsItem {

        @JSONField(name = "dcity")
        String dcity;

        @JSONField(name = "date")
        String date;

        @JSONField(name = "dcityname")
        String dcityname = "";

        @JSONField(name = "dcityid")
        int dcityid = 258;

        @JSONField(name = "acityname")
        String acityname = "";

        @JSONField(name = "acity")
        String acity;

        @JSONField(name = "aport")
        String aport = "";

        @JSONField(name = "acityid")
        int acityid = 1;

        @JSONField(name = "aportname")
        String aportname = "";
    }
}