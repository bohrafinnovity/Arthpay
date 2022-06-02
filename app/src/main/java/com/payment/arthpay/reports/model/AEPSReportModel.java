package com.payment.arthpay.reports.model;

import com.google.gson.annotations.SerializedName;

public class AEPSReportModel {

    @SerializedName("api_id")
    private String apiId;

    @SerializedName("mdid")
    private String mdid;

    @SerializedName("mytxnid")
    private String mytxnid;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("remark")
    private String remark;

    @SerializedName("type")
    private String type;

    @SerializedName("TxnMedium")
    private String txnMedium;

    @SerializedName("disprofit")
    private String disprofit;

    @SerializedName("via")
    private String via;

    @SerializedName("rtype")
    private String rtype;

    @SerializedName("bank")
    private String bank;

    @SerializedName("wid")
    private String wid;

    @SerializedName("balance")
    private double balance;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("apicode")
    private String apicode;

    @SerializedName("aadhar")
    private String aadhar;

    @SerializedName("id")
    private String id;

    @SerializedName("payid")
    private String payid;

    @SerializedName("disid")
    private String disid;

    @SerializedName("txnid")
    private String txnid;

    @SerializedName("refno")
    private String refno;

    @SerializedName("amount")
    private String amount;

    @SerializedName("product")
    private String product;

    @SerializedName("charge")
    private String charge;

    @SerializedName("wprofit")
    private String wprofit;

    @SerializedName("transtype")
    private String transtype;

    @SerializedName("credited_by")
    private String creditedBy;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("terminalid")
    private String terminalid;

    @SerializedName("authcode")
    private String authcode;

    @SerializedName("aepstype")
    private String aepstype;

    @SerializedName("mdprofit")
    private String mdprofit;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("apitxnid")
    private String apitxnid;

    @SerializedName("provider_id")
    private String providerId;

    @SerializedName("status")
    private String status;

    @SerializedName("username")
    private String username;

    @SerializedName("gst")
    private String gst;

    @SerializedName("tds")
    private String tds;


    private double closingAmount;
    private double showAmount;

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }

    public double getClosingAmount() {
        return closingAmount;
    }

    public void setClosingAmount(double closingAmount) {
        this.closingAmount = closingAmount;
    }

    public double getShowAmount() {
        return showAmount;
    }

    public void setShowAmount(double showAmount) {
        this.showAmount = showAmount;
    }

    public String getApiId() {
        return apiId;
    }

    public String getMdid() {
        return mdid;
    }

    public String getMytxnid() {
        return mytxnid;
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

    public String getTxnMedium() {
        return txnMedium;
    }

    public String getDisprofit() {
        return disprofit;
    }

    public String getVia() {
        return via;
    }

    public String getRtype() {
        return rtype;
    }

    public String getBank() {
        return bank;
    }

    public String getWid() {
        return wid;
    }

    public double getBalance() {
        return balance;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getApicode() {
        return apicode;
    }

    public String getAadhar() {
        return aadhar;
    }

    public String getId() {
        return id;
    }

    public String getPayid() {
        return payid;
    }

    public String getDisid() {
        return disid;
    }

    public String getTxnid() {
        return txnid;
    }

    public String getRefno() {
        return refno;
    }

    public String getAmount() {
        return amount;
    }

    public String getProduct() {
        return product;
    }

    public String getCharge() {
        return charge;
    }

    public String getWprofit() {
        return wprofit;
    }

    public String getTranstype() {
        return transtype;
    }

    public String getCreditedBy() {
        return creditedBy;
    }

    public String getMobile() {
        return mobile;
    }

    public String getTerminalid() {
        return terminalid;
    }

    public String getAuthcode() {
        return authcode;
    }

    public String getAepstype() {
        return aepstype;
    }

    public String getMdprofit() {
        return mdprofit;
    }

    public String getUserId() {
        return userId;
    }

    public String getApitxnid() {
        return apitxnid;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }
}