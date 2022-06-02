package com.payment.arthpay.RechargePlans.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlanResponse {

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("status")
    private String status;

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    public List<DataItem> getData() {
        return data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return
                "PlanResponse{" +
                        "data = '" + data + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}