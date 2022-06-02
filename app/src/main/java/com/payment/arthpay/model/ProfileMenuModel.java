package com.payment.arthpay.model;

public class ProfileMenuModel {
    private int imgUrl;
    private String menuName;

    public ProfileMenuModel(int imgUrl, String menuName) {
        this.imgUrl = imgUrl;
        this.menuName = menuName;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
