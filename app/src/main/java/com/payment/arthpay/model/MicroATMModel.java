package com.payment.arthpay.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MicroATMModel implements Parcelable {

    @SerializedName("requesttxn")
    @Expose
    private String requesttxn;
    @SerializedName("refstan")
    @Expose
    private String refstan;
    @SerializedName("txnamount")
    @Expose
    private String txnamount;
    @SerializedName("mid")
    @Expose
    private String mid;
    @SerializedName("tid")
    @Expose
    private String tid;
    @SerializedName("clientrefid")
    @Expose
    private String clientrefid;
    @SerializedName("vendorid")
    @Expose
    private String vendorid;
    @SerializedName("udf1")
    @Expose
    private String udf1;
    @SerializedName("udf2")
    @Expose
    private String udf2;
    @SerializedName("udf3")
    @Expose
    private String udf3;
    @SerializedName("udf4")
    @Expose
    private String udf4;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("bankremarks")
    @Expose
    private String bankremarks;
    @SerializedName("cardno")
    @Expose
    private String cardno;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("invoicenumber")
    @Expose
    private String invoicenumber;
    @SerializedName("rrn")
    @Expose
    private String rrn;
    @SerializedName("statuscode")
    @Expose
    private String statuscode;

    public MicroATMModel(String requesttxn, String refstan, String txnamount, String mid, String tid, String clientrefid, String vendorid, String udf1, String udf2, String udf3, String udf4, String date, String bankremarks, String cardno, String amount, String invoicenumber, String rrn, String statuscode, String message) {

        this.requesttxn = requesttxn;
        this.refstan = refstan;
        this.txnamount = txnamount;
        this.mid = mid;
        this.tid = tid;
        this.clientrefid = clientrefid;
        this.vendorid = vendorid;
        this.udf1 = udf1;
        this.udf2 = udf2;
        this.udf3 = udf3;
        this.udf4 = udf4;
        this.date = date;
        this.bankremarks = bankremarks;
        this.cardno = cardno;
        this.amount = amount;
        this.invoicenumber = invoicenumber;
        this.rrn = rrn;
        this.statuscode = statuscode;
        this.message = message;
    }

    @SerializedName("message")
    @Expose
    private String message;

    protected MicroATMModel(Parcel in) {
        requesttxn = in.readString();
        refstan = in.readString();
        txnamount = in.readString();
        mid = in.readString();
        tid = in.readString();
        clientrefid = in.readString();
        vendorid = in.readString();
        udf1 = in.readString();
        udf2 = in.readString();
        udf3 = in.readString();
        udf4 = in.readString();
        date = in.readString();
        bankremarks = in.readString();
        cardno = in.readString();
        amount = in.readString();
        invoicenumber = in.readString();
        rrn = in.readString();
        statuscode = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(requesttxn);
        dest.writeString(refstan);
        dest.writeString(txnamount);
        dest.writeString(mid);
        dest.writeString(tid);
        dest.writeString(clientrefid);
        dest.writeString(vendorid);
        dest.writeString(udf1);
        dest.writeString(udf2);
        dest.writeString(udf3);
        dest.writeString(udf4);
        dest.writeString(date);
        dest.writeString(bankremarks);
        dest.writeString(cardno);
        dest.writeString(amount);
        dest.writeString(invoicenumber);
        dest.writeString(rrn);
        dest.writeString(statuscode);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MicroATMModel> CREATOR = new Creator<MicroATMModel>() {
        @Override
        public MicroATMModel createFromParcel(Parcel in) {
            return new MicroATMModel(in);
        }

        @Override
        public MicroATMModel[] newArray(int size) {
            return new MicroATMModel[size];
        }
    };

    public String getRequesttxn() {
        return requesttxn;
    }

    public void setRequesttxn(String requesttxn) {
        this.requesttxn = requesttxn;
    }

    public String getRefstan() {
        return refstan;
    }

    public void setRefstan(String refstan) {
        this.refstan = refstan;
    }

    public String getTxnamount() {
        return txnamount;
    }

    public void setTxnamount(String txnamount) {
        this.txnamount = txnamount;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getClientrefid() {
        return clientrefid;
    }

    public void setClientrefid(String clientrefid) {
        this.clientrefid = clientrefid;
    }

    public String getVendorid() {
        return vendorid;
    }

    public void setVendorid(String vendorid) {
        this.vendorid = vendorid;
    }

    public String getUdf1() {
        return udf1;
    }

    public void setUdf1(String udf1) {
        this.udf1 = udf1;
    }

    public String getUdf2() {
        return udf2;
    }

    public void setUdf2(String udf2) {
        this.udf2 = udf2;
    }

    public String getUdf3() {
        return udf3;
    }

    public void setUdf3(String udf3) {
        this.udf3 = udf3;
    }

    public String getUdf4() {
        return udf4;
    }

    public void setUdf4(String udf4) {
        this.udf4 = udf4;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBankremarks() {
        return bankremarks;
    }

    public void setBankremarks(String bankremarks) {
        this.bankremarks = bankremarks;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInvoicenumber() {
        return invoicenumber;
    }

    public void setInvoicenumber(String invoicenumber) {
        this.invoicenumber = invoicenumber;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
