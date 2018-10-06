package com.jerryc05.crawl_ctrip_by_json;

import com.alibaba.fastjson.annotation.JSONField;

public class ProductsJsonPost {

    @JSONField(name = "flightWay")
    String flightWay = "Oneway";

    @JSONField(name = "classType")
    String classType = "ALL";

    @JSONField(name = "searchIndex")
    int searchIndex = 1;

    @JSONField(name = "airportParams")
    AirportParamsItem[] airportParams;

    public class AirportParamsItem {

        @JSONField(name = "dcity")
        String dcity;

        @JSONField(name = "acity")
        String acity;

        @JSONField(name = "dcityname")
        String dcityname;

        @JSONField(name = "acityname")
        String acityname;

        @JSONField(name = "date")
        String date;

        public String getDcity() {
            return dcity;
        }

        public String getDate() {
            return date;
        }

        public String getDcityname() {
            return dcityname;
        }

        public String getAcityname() {
            return acityname;
        }

        public String getAcity() {
            return acity;
        }

        public void setDcity(String dcity) {
            this.dcity = dcity;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setDcityname(String dcityname) {
            this.dcityname = dcityname;
        }

        public void setAcityname(String acityname) {
            this.acityname = acityname;
        }

        public void setAcity(String acity) {
            this.acity = acity;
        }
    }

    public AirportParamsItem[] getAirportParams() {
        return airportParams;
    }

    public void setAirportParams(AirportParamsItem[] airportParams) {
        this.airportParams = airportParams;
    }

    public int getSearchIndex() {
        return searchIndex;
    }

    public void setSearchIndex(int searchIndex) {
        this.searchIndex = searchIndex;
    }

    public String getFlightWay() {
        return flightWay;
    }

    public void setFlightWay(String flightWay) {
        this.flightWay = flightWay;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }
}