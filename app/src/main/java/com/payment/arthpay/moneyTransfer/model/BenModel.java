package com.payment.arthpay.moneyTransfer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BenModel implements Parcelable {

	@SerializedName("benemobile")
	private String benemobile;

	@SerializedName("beneaccount")
	private String beneaccount;

	@SerializedName("benename")
	private String benename;

	@SerializedName("beneid")
	private String beneid;

	@SerializedName("benebankid")
	private String benebankid;

	@SerializedName("beneifsc")
	private String beneifsc;

	@SerializedName("benebank")
	private String benebank;

	@SerializedName("benestatus")
	private String benestatus;

	protected BenModel(Parcel in) {
		benemobile = in.readString();
		beneaccount = in.readString();
		benename = in.readString();
		beneid = in.readString();
		benebankid = in.readString();
		beneifsc = in.readString();
		benebank = in.readString();
		benestatus = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(benemobile);
		dest.writeString(beneaccount);
		dest.writeString(benename);
		dest.writeString(beneid);
		dest.writeString(benebankid);
		dest.writeString(beneifsc);
		dest.writeString(benebank);
		dest.writeString(benestatus);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<BenModel> CREATOR = new Creator<BenModel>() {
		@Override
		public BenModel createFromParcel(Parcel in) {
			return new BenModel(in);
		}

		@Override
		public BenModel[] newArray(int size) {
			return new BenModel[size];
		}
	};

	public String getBenemobile(){
		return benemobile;
	}

	public String getBeneaccount(){
		return beneaccount;
	}

	public String getBenename(){
		return benename;
	}

	public String getBeneid(){
		return beneid;
	}

	public String getBenebankid(){
		return benebankid;
	}

	public String getBeneifsc(){
		return beneifsc;
	}

	public String getBenebank(){
		return benebank;
	}

	public String getBenestatus(){
		return benestatus;
	}
}