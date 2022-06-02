package com.payment.arthpay.model;

public class CenterGridModel {
    private String topLbl;
    private int imgUrl;
    private String bottomLbl;

    public CenterGridModel(String topLbl, int imgUrl, String bottomLbl) {
        this.topLbl = topLbl;
        this.imgUrl = imgUrl;
        this.bottomLbl = bottomLbl;
    }

    public String getTopLbl() {
        return topLbl;
    }

    public void setTopLbl(String topLbl) {
        this.topLbl = topLbl;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBottomLbl() {
        return bottomLbl;
    }

    public void setBottomLbl(String bottomLbl) {
        this.bottomLbl = bottomLbl;
    }
}
