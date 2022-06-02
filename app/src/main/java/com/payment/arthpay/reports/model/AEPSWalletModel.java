package com.payment.arthpay.reports.model;

import com.google.gson.annotations.SerializedName;

public class AEPSWalletModel{

	@SerializedName("refno")
	private String refno;

	@SerializedName("amount")
	private String amount;

	@SerializedName("product")
	private String product;

	@SerializedName("charge")
	private String charge;

	@SerializedName("transtype")
	private String transtype;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("remark")
	private String remark;

	@SerializedName("type")
	private String type;

	@SerializedName("via")
	private String via;

	@SerializedName("aepstype")
	private String aepstype;

	@SerializedName("rtype")
	private String rtype;

	@SerializedName("bank")
	private String bank;

	@SerializedName("balance")
	private String balance;

	@SerializedName("apicode")
	private String apicode;

	@SerializedName("aadhar")
	private String aadhar;

	@SerializedName("id")
	private int id;

	@SerializedName("payid")
	private String payid;

	@SerializedName("txnid")
	private String txnid;

	@SerializedName("status")
	private String status;

	@SerializedName("username")
	private String username;

	@SerializedName("gst")
	private String gst;

	@SerializedName("tds")
	private String tds;

	private double closingAmount ;
	private double showAmount;

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

	public String getRefno(){
		return refno;
	}

	public String getAmount(){
		return amount;
	}

	public String getProduct(){
		return product;
	}

	public String getCharge(){
		return charge;
	}

	public String getTranstype(){
		return transtype;
	}

	public String getMobile(){
		return mobile;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getRemark(){
		return remark;
	}

	public String getType(){
		return type;
	}

	public String getVia(){
		return via;
	}

	public String getAepstype(){
		return aepstype;
	}

	public String getRtype(){
		return rtype;
	}

	public String getBank(){
		return bank;
	}

	public String getBalance(){
		return balance;
	}

	public String getApicode(){
		return apicode;
	}

	public String getAadhar(){
		return aadhar;
	}

	public int getId(){
		return id;
	}

	public String getPayid(){
		return payid;
	}

	public String getTxnid(){
		return txnid;
	}

	public String getStatus(){
		return status;
	}

	public String getUsername(){
		return username;
	}
}