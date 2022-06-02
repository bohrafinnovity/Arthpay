package com.payment.arthpay.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.payment.arthpay.R;


public class KYCStatus extends AppCompatActivity {

    private Button loginButton;
    private ImageView imgKyc;

    private void init() {
        //loginButton = findViewById(R.id.loginButton);
        imgKyc = findViewById(R.id.kycImage);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kyc_status_activity);

        init();

        Glide.with(this)
                .load("https://cdn.iconscout.com/icon/premium/png-256-thumb/biometric-1855131-1573215.png")
                //.centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .into(imgKyc);
    }
}
