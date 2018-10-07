package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

public class LowestPriceJsonReturned {

    @JSONField(name = "data")
    private DataReturned data;

    public DataReturned getData() {
        return data;
    }

    public void setData(DataReturned data) {
        this.data = data;
    }

}