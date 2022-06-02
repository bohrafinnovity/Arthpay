package com.payment.arthpay.model;

public class BankListItems {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getBank_icon() {
        return bank_icon;
    }

    public void setBank_icon(String bank_icon) {
        this.bank_icon = bank_icon;
    }

    private String id;
    private String bank;
    private String ifsc;
    private String bank_id;
    private String bank_icon;
}
