package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

public class RedirectMRoute {

    @JSONField(name = "openJaws")
    private FlightsItem[] openJaws;

    @JSONField(name = "roundNears")
    private FlightsItem[] roundNears;

    public FlightsItem[] getRoundNears() {
        return roundNears;
    }

    public void setRoundNears(FlightsItem[] roundNears) {
        this.roundNears = roundNears;
    }

    public FlightsItem[] getOpenJaws() {
        return openJaws;
    }

    public void setOpenJaws(FlightsItem[] openJaws) {
        this.openJaws = openJaws;
    }
}
