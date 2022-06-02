package com.payment.arthpay.on_boarding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.payment.arthpay.MainActivity;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.databinding.ActivityUploadDocumentBinding;
import com.payment.arthpay.httpRequest.VolleyPostNetworkCall;
import com.payment.arthpay.member.MyUtil;
import com.payment.arthpay.utill.AppHandler;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.ImageUtils;
import com.payment.arthpay.utill.Print;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UploadDocumentActivity extends AppCompatActivity implements VolleyPostNetworkCall.RequestResponseLis {
    ActivityUploadDocumentBinding binding;
    private ImageView addharFrontClickImg, panClickImg, addharBackClickImg, passportPicClickImg;
    private ImageView addharImg, panImg, shopImg, passportImg;
    private OnBoardingModel model;
    private int TYPE = 0;
    private File fileAadhaarImage = null;
    private File filePanImage = null;
    private File fileShopImage = null;
    private File filePassportImage = null;
    private String aadhaarImage;
    private String panImage;
    private String shopImage;
    private String passportImage;

    private void inti() {
        model = getIntent().getParcelableExtra("data");
        addharFrontClickImg = binding.adharFrontLayout.imgAttach;
        panClickImg = binding.panLayout.imgAttach;
        addharBackClickImg = binding.adhaarBackLayout.imgAttach;
        passportPicClickImg = binding.passportLayout.imgAttach;

        addharImg = binding.adharFrontLayout.imgPreview;
        panImg = binding.panLayout.imgPreview;
        shopImg = binding.adhaarBackLayout.imgPreview;
        passportImg = binding.passportLayout.imgPreview;

        binding.adharFrontLayout.tvLabel.setText("Aadhaar Front Image");
        binding.adhaarBackLayout.tvLabel.setText("Aadhaar Back Image");
        binding.panLayout.tvLabel.setText("Pancard Image");
        binding.passportLayout.tvLabel.setText("Passport Image");

        addharFrontClickImg.setOnClickListener(v -> {
            TYPE = 0;
            pickGalleryImage();
        });

        panClickImg.setOnClickListener(v -> {
            TYPE = 1;
            pickGalleryImage();
        });

        addharBackClickImg.setOnClickListener(v -> {
            TYPE = 2;
            pickGalleryImage();
        });

        passportPicClickImg.setOnClickListener(v -> {
            TYPE = 3;
            pickGalleryImage();
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadDocumentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inti();

        binding.submitDoc.setOnClickListener(v -> {
            if (isValid()) {
                networkCallUsingVolleyApi(Constents.URL.AEPS_REGISTRATION, true);
            }
        });
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            //new VolleyPostNetworkCall(this, this, url, 1, param(), isLoad).netWorkCall();
            new VolleyPostNetworkCall(this, this, url, isLoad).netWorkCall(param());
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        if (aadhaarImage != null && aadhaarImage.length() > 0) map.put("kyc2", aadhaarImage);
        if (panImage != null && panImage.length() > 0) map.put("kyc1", panImage);
        if (shopImage != null && shopImage.length() > 0) map.put("kyc3", shopImage);
        if (passportImage != null && passportImage.length() > 0) map.put("kyc4", passportImage);
        map.put("user_id", model.getUserId());
        map.put("bc_f_name", model.getFirstName());
        if (MyUtil.isNN(model.getMiddleName()))
            map.put("bc_m_name", model.getMiddleName());
        if (MyUtil.isNN(model.getLastName()))
            map.put("bc_l_name", model.getLastName());
        map.put("emailid", model.getEmail());
        map.put("phone1", model.getPhoneOne());
        map.put("phone2", model.getPhoneTwo());
        map.put("bc_dob", model.getDob());
        map.put("bc_state", model.getState());
        map.put("bc_district", model.getDistrict());
        map.put("bc_address", model.getAddress());
        map.put("bc_block", model.getBlock());
        map.put("bc_city", model.getCity());
        map.put("bc_landmark", model.getLandmark());
        map.put("bc_mohhalla", model.getMohalla());
        map.put("bc_loc", model.getLoc());
        map.put("bc_pincode", model.getPincode());
        map.put("bc_pan", model.getPan());
        map.put("shopname", model.getShopName());
        map.put("shopType", model.getShopType());
        map.put("qualification", model.getQualification());
        map.put("population", model.getPopulation());
        map.put("locationType", model.getLocType());

        String param = new JSONObject(map).toString();
        Print.P("PARAM : " + param);
        return map;
    }

    private void pickGalleryImage() {
        ImagePicker.Companion.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(100)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (TYPE == 0) {
                    fileAadhaarImage = ImagePicker.Companion.getFile(data);
                    aadhaarImage = ImageUtils.getBase64Image(fileAadhaarImage);
                    if (fileAadhaarImage != null) {
                        showImage(fileAadhaarImage, addharImg);
                    }
                } else if (TYPE == 1) {
                    filePanImage = ImagePicker.Companion.getFile(data);
                    panImage = ImageUtils.getBase64Image(filePanImage);
                    if (filePanImage != null) {
                        showImage(filePanImage, panImg);
                    }
                } else if (TYPE == 2) {
                    fileShopImage = ImagePicker.Companion.getFile(data);
                    shopImage = ImageUtils.getBase64Image(fileShopImage);
                    if (fileShopImage != null) {
                        showImage(fileShopImage, shopImg);
                    }
                } else if (TYPE == 3) {
                    filePassportImage = ImagePicker.Companion.getFile(data);
                    passportImage = ImageUtils.getBase64Image(filePassportImage);
                    if (filePassportImage != null) {
                        showImage(filePassportImage, passportImg);
                    }
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, "" + ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showImage(File file, ImageView view) {
        if (file.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            view.setImageBitmap(myBitmap);
        }
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P("On Boarding : " + JSonResponse);
        try {
            JSONObject obj = new JSONObject(JSonResponse);
            String message = obj.getString("message");
            if (AppHandler.checkStatus(JSonResponse, UploadDocumentActivity.this)) {
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage(message);
                builder1.setCancelable(false)
                        .setTitle("KYC Status")
                        .setMessage(message)
                        .setPositiveButton("Ok", (dialog, i) -> {
                            dialog.dismiss();
                            startActivity(new Intent(this, MainActivity.class));
                            finishAffinity();
                        });
                AlertDialog alert = builder1.create();
                alert.show();
            } else {
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onFailRequest(String msg) {
        showMsg(msg);
    }

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean isValid() {
        if (filePanImage == null || !filePanImage.exists()) {
            Toast.makeText(this, "Pancard image is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fileAadhaarImage == null || !fileAadhaarImage.exists()) {
            Toast.makeText(this, "Aadhaar image is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (fileShopImage == null || !fileShopImage.exists()) {
            Toast.makeText(this, "Shop image is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (filePassportImage == null || !filePassportImage.exists()) {
            Toast.makeText(this, "Passport Size image is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}