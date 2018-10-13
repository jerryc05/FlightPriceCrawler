package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

public class Price {

    @JSONField(name = "price")
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
