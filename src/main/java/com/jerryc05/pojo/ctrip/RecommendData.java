package com.jerryc05.pojo.ctrip;


import com.alibaba.fastjson.annotation.JSONField;

public class RecommendData {

    @JSONField(name = "redirectSingleProduct")
    private RedirectSingleProduct redirectSingleProduct;

    public RedirectSingleProduct getRedirectSingleProduct() {
        return redirectSingleProduct;
    }

    public void setRedirectSingleProduct(RedirectSingleProduct redirectSingleProduct) {
        this.redirectSingleProduct = redirectSingleProduct;
    }
}