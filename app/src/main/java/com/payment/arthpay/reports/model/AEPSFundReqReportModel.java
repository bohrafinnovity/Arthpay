package com.payment.arthpay.reports.model;

import com.google.gson.annotations.SerializedName;

public class AEPSFundReqReportModel {

    @SerializedName("amount")
    private String amount;

    @SerializedName("payoutref")
    private String payoutref;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("remark")
    private String remark;

    @SerializedName("type")
    private String type;

    @SerializedName("payoutid")
    private String payoutid;

    @SerializedName("bank")
    private String bank;

    @SerializedName("requesttype")
    private String requesttype;

    @SerializedName("pay_type")
    private String payType;

    @SerializedName("id")
    private String id;

    @SerializedName("ifsc")
    private String ifsc;

    @SerializedName("openref")
    private String openref;

    @SerializedName("account")
    private String account;

    @SerializedName("status")
    private String status;

    @SerializedName("mode")
    private String mode;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAmount() {
        return amount;
    }

    public String getPayoutref() {
        return payoutref;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getRemark() {
        return remark;
    }

    public String getType() {
        return type;
    }

    public String getPayoutid() {
        return payoutid;
    }

    public String getBank() {
        return bank;
    }

    public String getRequesttype() {
        return requesttype;
    }

    public String getPayType() {
        return payType;
    }

    public String getId() {
        return id;
    }

    public String getIfsc() {
        return ifsc;
    }

    public String getOpenref() {
        return openref;
    }

    public String getAccount() {
        return account;
    }

    public String getStatus() {
        return status;
    }
}