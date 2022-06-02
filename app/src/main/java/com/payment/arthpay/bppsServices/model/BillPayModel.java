package com.payment.arthpay.bppsServices.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillPayModel{

	@SerializedName("api_id")
	private int apiId;

	@SerializedName("manditcount")
	private String manditcount;

	@SerializedName("parame")
	private List<ParameItem> parame;

	@SerializedName("recharge2")
	private String recharge2;

	@SerializedName("recharge1")
	private String recharge1;

	@SerializedName("name")
	private String name;

	@SerializedName("logo")
	private String logo;

	@SerializedName("id")
	private int id;

	@SerializedName("paramcount")
	private String paramcount;

	@SerializedName("state")
	private int state;

	@SerializedName("type")
	private String type;

	@SerializedName("status")
	private String status;

	public int getApiId(){
		return apiId;
	}

	public String getManditcount(){
		return manditcount;
	}

	public List<ParameItem> getParame(){
		return parame;
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

	public int getId(){
		return id;
	}

	public String getParamcount(){
		return paramcount;
	}

	public int getState(){
		return state;
	}

	public String getType(){
		return type;
	}

	public String getStatus(){
		return status;
	}
}