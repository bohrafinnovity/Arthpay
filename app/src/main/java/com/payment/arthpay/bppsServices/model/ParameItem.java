package com.payment.arthpay.bppsServices.model;

import com.google.gson.annotations.SerializedName;

public class ParameItem{

	@SerializedName("ismandatory")
	private String ismandatory;

	@SerializedName("minlength")
	private String minlength;

	@SerializedName("regex")
	private String regex;

	@SerializedName("maxlength")
	private String maxlength;

	@SerializedName("paramname")
	private String paramname;

	@SerializedName("fieldtype")
	private String fieldtype;

	private String fieldInputValue ;

	public String getIsmandatory(){
		return ismandatory;
	}

	public String getMinlength(){
		return minlength;
	}

	public String getRegex(){
		return regex;
	}

	public String getMaxlength(){
		return maxlength;
	}

	public String getParamname(){
		return paramname;
	}

	public String getFieldtype(){
		return fieldtype;
	}

	public String getFieldInputValue() {
		return fieldInputValue;
	}

	public void setFieldInputValue(String fieldInputValue) {
		this.fieldInputValue = fieldInputValue;
	}
}