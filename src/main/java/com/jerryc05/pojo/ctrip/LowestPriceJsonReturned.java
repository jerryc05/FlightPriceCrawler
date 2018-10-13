package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

public class LowestPriceJsonReturned {

    @JSONField(name = "data")
    private DataLowestProce data;

    public DataLowestProce getData() {
        return data;
    }

    public void setData(DataLowestProce data) {
        this.data = data;
    }

}