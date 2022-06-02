package com.payment.arthpay.reports.model;

import com.google.gson.annotations.SerializedName;

public class DMTModel {

    @SerializedName("refno")
    private String refno;

    @SerializedName("amount")
    private String amount;

    @SerializedName("tds")
    private String tds;

    @SerializedName("charge")
    private String charge;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("gst")
    private String gst;

    @SerializedName("description")
    private String description;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("remark")
    private String remark;

    @SerializedName("adminprofit")
    private String adminprofit;

    @SerializedName("via")
    private String via;

    @SerializedName("number")
    private String number;

    @SerializedName("rtype")
    private String rtype;

    @SerializedName("balance")
    private double balance;

    @SerializedName("option3")
    private String option3;

    @SerializedName("option4")
    private String option4;

    @SerializedName("option1")
    private String option1;

    @SerializedName("id")
    private String id;

    @SerializedName("option2")
    private String option2;

    @SerializedName("profit")
    private String profit;

    @SerializedName("txnid")
    private String txnid;

    @SerializedName("status")
    private String status;

    @SerializedName("trans_type")
    private String transType;

    public String getRefno() {
        return refno;
    }

    public String getAmount() {
        return amount;
    }

    public String getTds() {
        return tds;
    }

    public String getCharge() {
        return charge;
    }

    public String getMobile() {
        return mobile;
    }

    public String getGst() {
        return gst;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getRemark() {
        return remark;
    }

    public String getAdminprofit() {
        return adminprofit;
    }

    public String getVia() {
        return via;
    }

    public String getNumber() {
        return number;
    }

    public String getRtype() {
        return rtype;
    }

    public double getBalance() {
        return balance;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getOption1() {
        return option1;
    }

    public String getId() {
        return id;
    }

    public String getOption2() {
        return option2;
    }

    public String getProfit() {
        return profit;
    }

    public String getTxnid() {
        return txnid;
    }

    public String getStatus() {
        return status;
    }

    public String getTransType() {
        return transType;
    }
}