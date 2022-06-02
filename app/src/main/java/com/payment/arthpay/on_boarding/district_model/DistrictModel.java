package com.payment.arthpay.on_boarding.district_model;

public class DistrictModel {
    private String districtid;
    private String districtname;

    public DistrictModel(String districtid, String districtname) {
        this.districtid = districtid;
        this.districtname = districtname;
    }

    public String getDistrictid() {
        return districtid;
    }

    public void setDistrictid(String districtid) {
        this.districtid = districtid;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }
}
