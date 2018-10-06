package com.jerryc05.crawl_ctrip_by_json;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

class LowestPriceJsonReturned {

    @JSONField(name = "data")
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    class Data {

        @JSONField(name = "oneWayPrice")
        private Map<String, Integer>[] oneWayPrice;

        @JSONField(name = "roundTripPrice")
        private Map<String, Map<String, Integer>> roundTripPrice;

        @JSONField(name = "singleToRoundPrice")
        private Object singleToRoundPrice;

        public Map<String, Integer>[] getOneWayPrice() {
            return oneWayPrice;
        }

        public void setOneWayPrice(Map<String, Integer>[] oneWayPrice) {
            this.oneWayPrice = oneWayPrice;
        }

        public Map<String, Map<String, Integer>> getRoundTripPrice() {
            return roundTripPrice;
        }

        public void setRoundTripPrice(Map<String, Map<String, Integer>> roundTripPrice) {
            this.roundTripPrice = roundTripPrice;
        }

        public Object getSingleToRoundPrice() {
            return singleToRoundPrice;
        }

        public void setSingleToRoundPrice(Object singleToRoundPrice) {
            this.singleToRoundPrice = singleToRoundPrice;
        }
    }
}