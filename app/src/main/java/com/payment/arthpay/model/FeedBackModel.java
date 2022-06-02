package com.payment.arthpay.model;

import com.android.volley.VolleyLog;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class FeedBackModel {
    private String chatId;
    private String imagePath;
    private String farmer_Id;
    private String appVersion;
    private String device_imei_no;
    private String deviceDateTime;
    private String latitude;
    private String longitude;

    public FeedBackModel(String chatId, String imagePath, String farmer_Id, String deviceDateTime,
                         String latitude, String longitude, String device_imei_no, String appVersion) {
        this.chatId = chatId;
        this.imagePath = imagePath;
        this.farmer_Id = farmer_Id;
        this.appVersion = appVersion;
        this.device_imei_no = device_imei_no;
        this.deviceDateTime = deviceDateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFarmer_Id() {
        return farmer_Id;
    }

    public void setFarmer_Id(String farmer_Id) {
        this.farmer_Id = farmer_Id;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDevice_imei_no() {
        return device_imei_no;
    }

    public void setDevice_imei_no(String device_imei_no) {
        this.device_imei_no = device_imei_no;
    }

    public String getDeviceDateTime() {
        return deviceDateTime;
    }

    public void setDeviceDateTime(String deviceDateTime) {
        this.deviceDateTime = deviceDateTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void getFeedBackImageParam(MultipartEntity entity, File mFilePart) {
        try {
            entity.addPart("image_file", new FileBody(mFilePart));
            //System.out.println("image_file " + mFilePart.toString());

            entity.addPart("help_chat_id", new StringBody(this.chatId));
            //System.out.println("help_chat_id " + this.chatId);

            entity.addPart("farmer_id", new StringBody(this.farmer_Id));
            //System.out.println("farmer_id " + this.chatId);

            entity.addPart("latitude", new StringBody(this.latitude));
            //System.out.println("latitude " + this.latitude);

            entity.addPart("longitude", new StringBody(this.longitude));
            //System.out.println("longitude " + this.longitude);

            entity.addPart("device_datetime", new StringBody(this.deviceDateTime));
            //System.out.println("device_datetime " + this.deviceDateTime);

            entity.addPart("device_imei_no", new StringBody(this.device_imei_no));
            entity.addPart("app_version", new StringBody(this.appVersion));
            //////////////////////Extended Field/////////////////////////////////////
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }

    }
}
