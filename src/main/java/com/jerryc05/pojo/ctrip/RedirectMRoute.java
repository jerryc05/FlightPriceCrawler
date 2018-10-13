package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

public class RedirectMRoute {

    @JSONField(name = "openJaws")
    private RoundNearsOpenJawsItem[] openJaws;

    @JSONField(name = "roundNears")
    private RoundNearsOpenJawsItem[] roundNears;

    public RoundNearsOpenJawsItem[] getRoundNears() {
        return roundNears;
    }

    public void setRoundNears(RoundNearsOpenJawsItem[] roundNears) {
        this.roundNears = roundNears;
    }

    public RoundNearsOpenJawsItem[] getOpenJaws() {
        return openJaws;
    }

    public void setOpenJaws(RoundNearsOpenJawsItem[] openJaws) {
        this.openJaws = openJaws;
    }
}
