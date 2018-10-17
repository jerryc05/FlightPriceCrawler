package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

public class Characteristic {

    @JSONField(name = "lowestPrice")
    private Integer lowestPrice;

    @JSONField(name = "roundTripDiscounts")
    private boolean roundTripDiscounts;

    public Integer getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Integer lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public boolean isRoundTripDiscounts() {
        return roundTripDiscounts;
    }

    public void setRoundTripDiscounts(boolean roundTripDiscounts) {
        this.roundTripDiscounts = roundTripDiscounts;
    }
}
