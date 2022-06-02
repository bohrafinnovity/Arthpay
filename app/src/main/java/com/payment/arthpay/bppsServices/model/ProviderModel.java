package com.payment.arthpay.bppsServices.model;

import com.google.gson.annotations.SerializedName;

public class ProviderModel{

	@SerializedName("api_id")
	private String apiId;

	@SerializedName("manditcount")
	private String manditcount;

	@SerializedName("recharge2")
	private String recharge2;

	@SerializedName("recharge1")
	private String recharge1;

	@SerializedName("name")
	private String name;

	@SerializedName("logo")
	private String logo;

	@SerializedName("id")
	private String id;

	@SerializedName("paramcount")
	private String paramcount;

	@SerializedName("type")
	private String type;

	@SerializedName("status")
	private String status;

	public String getApiId(){
		return apiId;
	}

	public String getManditcount(){
		return manditcount;
	}

	public String getRecharge2(){
		return recharge2;
	}

	public String getRecharge1(){
		return recharge1;
	}

	public String getName(){
		return name;
	}

	public String getLogo(){
		return logo;
	}

	public String getId(){
		return id;
	}

	public String getParamcount(){
		return paramcount;
	}

	public String getType(){
		return type;
	}

	public String getStatus(){
		return status;
	}
}