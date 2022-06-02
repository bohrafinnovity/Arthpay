package com.payment.arthpay.reports.model;

import com.google.gson.annotations.SerializedName;

public class FundRequestModel{

	@SerializedName("amount")
	private String amount;

	@SerializedName("paymode")
	private String paymode;

	@SerializedName("payslip")
	private Object payslip;

	@SerializedName("actual_amount")
	private String actualAmount;

	@SerializedName("ref_no")
	private String refNo;

	@SerializedName("remark")
	private String remark;

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

	@SerializedName("paydate")
	private String paydate;

	@SerializedName("fundbank_id")
	private String fundbankId;

	@SerializedName("status")
	private String status;

	public String getAmount(){
		return amount;
	}

	public String getPaymode(){
		return paymode;
	}

	public Object getPayslip(){
		return payslip;
	}

	public String getActualAmount(){
		return actualAmount;
	}

	public String getRefNo(){
		return refNo;
	}

	public String getRemark(){
		return remark;
	}

	public String getId(){
		return id;
	}

	public String getType(){
		return type;
	}

	public String getPaydate(){
		return paydate;
	}

	public String getFundbankId(){
		return fundbankId;
	}

	public String getStatus(){
		return status;
	}
}