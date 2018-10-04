package com.jerryc05.crawl_ctrip_by_json;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

public class LowestPriceJsonGet {

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "status")
    private int status;

    @JSONField(name = "data")
    private Data data;

    public String getMsg() {
        return msg;
    }

    public Data getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    class Data {

        @JSONField(name = "oneWayPrice")
        private Map<String, Integer> oneWayPrice;

        @JSONField(name = "singleToRoundPrice")
        private Map<String, Integer> singleToRoundPrice;

        @JSONField(name = "roundTripPrice")
        private Map<String, Integer> roundTripPrice;

        public Map<String, Integer> getOneWayPrice() {
            return oneWayPrice;
        }

        public Map<String, Integer> getSingleToRoundPrice() {
            return singleToRoundPrice;
        }

        public Map<String, Integer> getRoundTripPrice() {
            return roundTripPrice;
        }
    }
}