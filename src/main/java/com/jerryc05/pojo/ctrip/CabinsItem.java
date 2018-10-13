package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

public class CabinsItem {

    @JSONField(name = "price")
    private Price price;

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
