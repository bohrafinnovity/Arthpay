package com.payment.arthpay.commission.gsonModel;

import com.google.gson.annotations.SerializedName;

public class CommissionModel {
    @SerializedName("md")
    private String md;

    @SerializedName("distributor")
    private String distributor;

    @SerializedName("retailer")
    private String retailer;

    @SerializedName("type")
    private String type;

    @SerializedName("provider")
    private Provider provider;
    private String  keys;

    public String getMd() {
        return md;
    }

    public void setMd(String md) {
        this.md = md;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRetailer() {
        return retailer;
    }

    public String getType() {
        return type;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String key) {
        this.keys = key;
    }
}