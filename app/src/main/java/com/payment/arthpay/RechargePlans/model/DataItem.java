package com.payment.arthpay.RechargePlans.model;

import com.google.gson.annotations.SerializedName;

public class DataItem {

    @SerializedName("amount")
    private String amount;

    @SerializedName("detail")
    private String detail;

    @SerializedName("validity")
    private String validity;
    private String parentText;
    private String lastUpdate;

    public DataItem(String amount, String detail, String validity, String parentText, String lastUpdate) {
        this.amount = amount;
        this.detail = detail;
        this.validity = validity;
        this.parentText = parentText;
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getParentText() {
        return parentText;
    }

    public void setParentText(String parentText) {
        this.parentText = parentText;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }


    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getValidity() {
        return validity;
    }


}