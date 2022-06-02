package com.payment.arthpay.on_boarding;

import android.os.Parcel;
import android.os.Parcelable;

public class OnBoardingModel implements Parcelable {
    private String userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phoneOne;
    private String phoneTwo;
    private String dob;
    private String state;
    private String district;
    private String address;
    private String block;
    private String city;
    private String landmark;
    private String mohalla;
    private String locType;
    private String loc;
    private String pincode;
    private String pan;
    private String shopName;
    private String shopType;
    private String qualification;
    private String population;

    public OnBoardingModel() {
    }


    protected OnBoardingModel(Parcel in) {
        userId = in.readString();
        firstName = in.readString();
        middleName = in.readString();
        lastName = in.readString();
        email = in.readString();
        phoneOne = in.readString();
        phoneTwo = in.readString();
        dob = in.readString();
        state = in.readString();
        district = in.readString();
        address = in.readString();
        block = in.readString();
        city = in.readString();
        landmark = in.readString();
        mohalla = in.readString();
        locType = in.readString();
        loc = in.readString();
        pincode = in.readString();
        pan = in.readString();
        shopName = in.readString();
        shopType = in.readString();
        qualification = in.readString();
        population = in.readString();

    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(firstName);
        dest.writeString(middleName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(phoneOne);
        dest.writeString(phoneTwo);
        dest.writeString(dob);
        dest.writeString(state);
        dest.writeString(district);
        dest.writeString(address);
        dest.writeString(block);
        dest.writeString(city);
        dest.writeString(landmark);
        dest.writeString(mohalla);
        dest.writeString(locType);
        dest.writeString(loc);
        dest.writeString(pincode);
        dest.writeString(pan);
        dest.writeString(shopName);
        dest.writeString(shopType);
        dest.writeString(qualification);
        dest.writeString(population);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OnBoardingModel> CREATOR = new Creator<OnBoardingModel>() {
        @Override
        public OnBoardingModel createFromParcel(Parcel in) {
            return new OnBoardingModel(in);
        }

        @Override
        public OnBoardingModel[] newArray(int size) {
            return new OnBoardingModel[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneOne() {
        return phoneOne;
    }

    public void setPhoneOne(String phoneOne) {
        this.phoneOne = phoneOne;
    }

    public String getPhoneTwo() {
        return phoneTwo;
    }

    public void setPhoneTwo(String phoneTwo) {
        this.phoneTwo = phoneTwo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getMohalla() {
        return mohalla;
    }

    public void setMohalla(String mohalla) {
        this.mohalla = mohalla;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public static Creator<OnBoardingModel> getCREATOR() {
        return CREATOR;
    }
}
