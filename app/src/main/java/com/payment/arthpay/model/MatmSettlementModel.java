package com.payment.arthpay.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatmSettlementModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("bank")
    @Expose
    private String bank;
    @SerializedName("account")
    @Expose
    private String account;
    @SerializedName("ifsc")
    @Expose
    private String ifsc;
    @SerializedName("apitxnid")
    @Expose
    private String apitxnid;
    @SerializedName("pay_type")
    @Expose
    private String payType;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("payoutid")
    @Expose
    private String payoutid;
    @SerializedName("payoutref")
    @Expose
    private String payoutref;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("username")
    @Expose
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getApitxnid() {
        return apitxnid;
    }

    public void setApitxnid(String apitxnid) {
        this.apitxnid = apitxnid;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPayoutid() {
        return payoutid;
    }

    public void setPayoutid(String payoutid) {
        this.payoutid = payoutid;
    }

    public String getPayoutref() {
        return payoutref;
    }

    public void setPayoutref(String payoutref) {
        this.payoutref = payoutref;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}