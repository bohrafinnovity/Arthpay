package com.payment.arthpay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.payment.arthpay.MainActivity;
import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.httpRequest.UpdateBillService;
import com.payment.arthpay.utill.PrefLoginManager;

import static com.payment.arthpay.utill.AppManager.getImei;


public class SplashScreen extends AppCompatActivity implements UpdateBillService.OnComplete {
    Thread splashTimer;
    TextView requestName;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        //Sprite doubleBounce = new Wave();
        //progressBar.setIndeterminateDrawable(doubleBounce);
        Constents.MOBILE_ID = getImei(SplashScreen.this);
        PrefLoginManager pref = new PrefLoginManager(this);
        String status = pref.getFarmerLoginRes();


        if (status != null && status.length() > 0) {
            new UpdateBillService(this, this);
        } else {
            startLoader(300);
        }

    }

    private void startLoader(final int maxTime) {
        splashTimer = new Thread() {
            public void run() {
                try {
                    int splashTime = 0;
                    int selector = 1;
                    while (splashTime < maxTime) {
                        Thread.sleep(700);
                        splashTime = splashTime + 100;
                    }

                    openActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        splashTimer.start();
    }


    private void openActivity() {
        startActivity(new Intent(this, Login.class));
        finish();
    }

    @Override
    public void onComplete() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
