package com.payment.arthpay.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BeneficiaryItems implements Parcelable {

    public static final Creator<BeneficiaryItems> CREATOR = new Creator<BeneficiaryItems>() {
        @Override
        public BeneficiaryItems createFromParcel(Parcel in) {
            return new BeneficiaryItems(in);
        }

        @Override
        public BeneficiaryItems[] newArray(int size) {
            return new BeneficiaryItems[size];
        }
    };
    private String beneid;
    private String benename;
    private String beneaccount;
    private String benebank;
    private String beneifsc;
    private String benebankid;
    private String benestatus;
    private String benemobile;
    private String sendername;
    private String sendernumber;

    public BeneficiaryItems() {
    }

    protected BeneficiaryItems(Parcel in) {
        beneid = in.readString();
        benename = in.readString();
        beneaccount = in.readString();
        benebank = in.readString();
        beneifsc = in.readString();
        benebankid = in.readString();
        benestatus = in.readString();
        benemobile = in.readString();
        sendername = in.readString();
        sendernumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(beneid);
        dest.writeString(benename);
        dest.writeString(beneaccount);
        dest.writeString(benebank);
        dest.writeString(beneifsc);
        dest.writeString(benebankid);
        dest.writeString(benestatus);
        dest.writeString(benemobile);
        dest.writeString(sendername);
        dest.writeString(sendernumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getBenebankid() {
        return benebankid;
    }

    public void setBenebankid(String benebankid) {
        this.benebankid = benebankid;
    }

    public String getBeneid() {
        return beneid;
    }

    public void setBeneid(String beneid) {
        this.beneid = beneid;
    }

    public String getBenemobile() {
        return benemobile;
    }

    public void setBenemobile(String benemobile) {
        this.benemobile = benemobile;
    }

    public String getBenename() {
        return benename;
    }

    public void setBenename(String benename) {
        this.benename = benename;
    }

    public String getBeneaccount() {
        return beneaccount;
    }

    public void setBeneaccount(String beneaccount) {
        this.beneaccount = beneaccount;
    }

    public String getBenebank() {
        return benebank;
    }

    public void setBenebank(String benebank) {
        this.benebank = benebank;
    }

    public String getBeneifsc() {
        return beneifsc;
    }

    public void setBeneifsc(String beneifsc) {
        this.beneifsc = beneifsc;
    }

    public String getBenestatus() {
        return benestatus;
    }

    public void setBenestatus(String benestatus) {
        this.benestatus = benestatus;
    }

    public String getSendernumber() {
        return sendernumber;
    }

    public void setSendernumber(String sendernumber) {
        this.sendernumber = sendernumber;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }
}
