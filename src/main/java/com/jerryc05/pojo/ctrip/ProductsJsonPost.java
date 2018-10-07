package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

public class ProductsJsonPost {

    @JSONField(name = "flightWay")
    private String flightWay = "Oneway";

    @JSONField(name = "searchIndex")
    private int searchIndex;

    @JSONField(name = "airportParams")
    private AirportParamsItem[] airportParams;

    public class AirportParamsItem {

        @JSONField(name = "dcity")
        private String dcity;

        @JSONField(name = "acity")
        private String acity;

        @JSONField(name = "date")
        private String date;

        public String getDcity() {
            return dcity;
        }

        public String getDate() {
            return date;
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
}