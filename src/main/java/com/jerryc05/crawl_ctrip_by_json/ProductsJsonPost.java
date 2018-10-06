package com.jerryc05.crawl_ctrip_by_json;

import com.alibaba.fastjson.annotation.JSONField;

@SuppressWarnings({"unused", "WeakerAccess"})
class ProductsJsonPost {

    @JSONField(name = "airportParams")
    AirportParamsItem[] airportParams;

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

    public AirportParamsItem[] getAirportParams() {
        return airportParams;
    }

    public boolean isHasBaby() {
        return hasBaby;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public int getSearchIndex() {
        return searchIndex;
    }

    public String getFlightWay() {
        return flightWay;
    }

    public String getClassType() {
        return classType;
    }

    @SuppressWarnings("unused")
    class AirportParamsItem {

        @JSONField(name = "dcity")
        String dcity;

        @JSONField(name = "date")
        String date;

        @JSONField(name = "dcityname")
        String dcityname;

        @JSONField(name = "dcityid")
        int dcityid;

        @JSONField(name = "acityname")
        String acityname;

        @JSONField(name = "acity")
        String acity;

        @JSONField(name = "aport")
        String aport;

        @JSONField(name = "acityid")
        int acityid;

        @JSONField(name = "aportname")
        String aportname;

        public String getDcity() {
            return dcity;
        }

        public String getDate() {
            return date;
        }

        public String getDcityname() {
            return dcityname;
        }

        public int getDcityid() {
            return dcityid;
        }

        public String getAcityname() {
            return acityname;
        }

        public String getAcity() {
            return acity;
        }

        public String getAport() {
            return aport;
        }

        public int getAcityid() {
            return acityid;
        }

        public String getAportname() {
            return aportname;
        }
    }
}