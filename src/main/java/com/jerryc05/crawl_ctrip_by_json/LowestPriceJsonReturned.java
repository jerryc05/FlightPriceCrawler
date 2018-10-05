package com.jerryc05.crawl_ctrip_by_json;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
import java.util.Map;

class LowestPriceJsonReturned {

//    @JSONField(name = "data")
//    Data data;

    @JSONField(name = "status")
    int status;

    @JSONField(name = "msg")
    String msg;

    int getStatus() {
        return status;
    }

//    Data getData() {
//        return data;
//    }
//
//    void setData(Data data) {
//        this.data = data;
//    }

    void setStatus(int status) {
        this.status = status;
    }

    String getMsg() {
        return msg;
    }

    void setMsg(String msg) {
        this.msg = msg;
    }

    class Data {

        @JSONField(name = "oneWayPrice")
        List<Map<String, Integer>> oneWayPrice;

        @JSONField(name = "roundTripPrice")
        Map<String, Map<String, Integer>> roundTripPrice;

        @JSONField(name = "singleToRoundPrice")
        Object singleToRoundPrice;

        List<Map<String, Integer>> getOneWayPrice() {
            return oneWayPrice;
        }

        void setOneWayPrice(List<Map<String, Integer>> oneWayPrice) {
            this.oneWayPrice = oneWayPrice;
        }

        Map<String, Map<String, Integer>> getRoundTripPrice() {
            return roundTripPrice;
        }

        void setRoundTripPrice(Map<String, Map<String, Integer>> roundTripPrice) {
            this.roundTripPrice = roundTripPrice;
        }

        Object getSingleToRoundPrice() {
            return singleToRoundPrice;
        }

        void setSingleToRoundPrice(Object singleToRoundPrice) {
            this.singleToRoundPrice = singleToRoundPrice;
        }
    }
}