package com.jerryc05.pojo.ctrip;


import com.alibaba.fastjson.annotation.JSONField;

public class RecommendData {

    @JSONField(name = "redirectSingleProduct")
    private RedirectSingleProduct redirectSingleProduct;

    @JSONField(name = "redirectMRoute")
    private RedirectMRoute redirectMRoute;

    public RedirectSingleProduct getRedirectSingleProduct() {
        return redirectSingleProduct;
    }

    public void setRedirectSingleProduct(RedirectSingleProduct redirectSingleProduct) {
        this.redirectSingleProduct = redirectSingleProduct;
    }

    public RedirectMRoute getRedirectMRoute() {
        return redirectMRoute;
    }

    public void setRedirectMRoute(RedirectMRoute redirectMRoute) {
        this.redirectMRoute = redirectMRoute;
    }
}