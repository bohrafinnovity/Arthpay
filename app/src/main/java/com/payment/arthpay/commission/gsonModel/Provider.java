package com.payment.arthpay.commission.gsonModel;

import com.google.gson.annotations.SerializedName;

public class Provider {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }
}