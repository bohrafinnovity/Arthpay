package com.payment.arthpay.member.model;

import com.google.gson.annotations.SerializedName;

public class AppMember {
    @SerializedName("city")
    private String city;

    @SerializedName("name")
    private String name;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("id")
    private String id;

    @SerializedName("email")
    private String email;
    @SerializedName("parents")
    private String parents;

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getParents() {
        return parents;
    }

    public void setParents(String parents) {
        this.parents = parents;
    }
}
