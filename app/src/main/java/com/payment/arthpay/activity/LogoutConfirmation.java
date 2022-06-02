package com.payment.arthpay.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.payment.arthpay.R;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.PrefLoginManager;

public class LogoutConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_confirmation);
        new PrefLoginManager(this).clearFarmerLoginRes();
        AppManager.getInstance().logoutApp(LogoutConfirmation.this);
    }
}
