package com.payment.arthpay.reports.model;

import com.google.gson.annotations.SerializedName;

public class WalletBankModel{

	@SerializedName("api_id")
	private String apiId;

	@SerializedName("refno")
	private String refno;

	@SerializedName("sendername")
	private String sendername;

	@SerializedName("amount")
	private String amount;

	@SerializedName("product")
	private String product;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("providername")
	private String providername;

	@SerializedName("number")
	private String number;

	@SerializedName("rtype")
	private String rtype;

	@SerializedName("balance")
	private String balance;

	@SerializedName("provider_id")
	private String providerId;

	@SerializedName("id")
	private String id;

	@SerializedName("payid")
	private String payid;

	@SerializedName("txnid")
	private String txnid;

	@SerializedName("status")
	private String status;

	@SerializedName("trans_type")
	private String transType;

	@SerializedName("username")
	private String username;

	@SerializedName("charge")
	private String charge;

	@SerializedName("profit")
	private String profit;

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

	public String getTds() {
		return tds;
	}

	public void setTds(String tds) {
		this.tds = tds;
	}

	public String getGst() {
		return gst;
	}

	public void setGst(String gst) {
		this.gst = gst;
	}

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getApiId(){
		return apiId;
	}

	public String getRefno(){
		return refno;
	}

	public String getSendername(){
		return sendername;
	}

	public String getAmount(){
		return amount;
	}

	public String getProduct(){
		return product;
	}

	public String getMobile(){
		return mobile;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getProvidername(){
		return providername;
	}

	public String getNumber(){
		return number;
	}

	public String getRtype(){
		return rtype;
	}

	public String getBalance(){
		return balance;
	}

	public String getProviderId(){
		return providerId;
	}

	public String getId(){
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

	public String getTransType(){
		return transType;
	}

	public String getUsername(){
		return username;
	}
}