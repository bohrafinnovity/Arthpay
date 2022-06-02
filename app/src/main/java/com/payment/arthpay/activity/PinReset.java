package com.payment.arthpay.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.httpRequest.VolleyGetNetworkCall;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONObject;


public class PinReset extends AppCompatActivity implements VolleyGetNetworkCall.RequestResponseLis {

    private Button btn;
    private EditText etNewPin, etConfirmPin, etOtp;
    private int REQUEST_TYPE = 0;

    private void init() {
        etNewPin = findViewById(R.id.etNewPin);
        etConfirmPin = findViewById(R.id.etConfirmPin);
        etOtp = findViewById(R.id.etOtp);
        btn = findViewById(R.id.btn);
        sendOTP();
    }

    private void sendOTP() {
        TextView tvPlan = findViewById(R.id.tvPlans);
        tvPlan.setOnClickListener(v -> {
            REQUEST_TYPE = 0;
            String url = Constents.URL.baseUrl + "api/android/tpin/getotp?apptoken=" +
                    SharedPrefs.getValue(PinReset.this, SharedPrefs.APP_TOKEN) +
                    "&user_id=" + SharedPrefs.getValue(PinReset.this, SharedPrefs.USER_ID) +
                    "&mobile=" + SharedPrefs.getValue(this, SharedPrefs.LOGIN_ID) +
                    "&device_id=" + Constents.MOBILE_ID;
            networkCallUsingVolleyApi(url);
        });
    }

    boolean isValid() {
        if (etNewPin.getText().toString().length() < 4) {
            Toast.makeText(this, "Pin length should be between 4 and 6", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etConfirmPin.getText().toString().length() < 4) {
            Toast.makeText(this, "Pin length should be between 4 and 6", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!etNewPin.getText().toString().equals(etConfirmPin.getText().toString())) {
            Toast.makeText(this, "Both pin should be same", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etOtp.getText().toString().length() == 0) {
            Toast.makeText(this, "otp field is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mpin_reset);
        init();

        btn.setOnClickListener(v -> {
            if (isValid()) {
                REQUEST_TYPE = 1;
                String url = Constents.URL.baseUrl + "api/android/tpin/generate?apptoken=" +
                        SharedPrefs.getValue(PinReset.this, SharedPrefs.APP_TOKEN) + "&user_id=" +
                        SharedPrefs.getValue(PinReset.this, SharedPrefs.USER_ID) +
                        "&tpin=" + etNewPin.getText().toString() +
                        "&mobile=" + SharedPrefs.getValue(this, SharedPrefs.LOGIN_ID) +
                        "&tpin_confirmation=" + etConfirmPin.getText().toString() +
                        "&otp=" + etOtp.getText().toString() +
                        "&device_id=" + Constents.MOBILE_ID;

                networkCallUsingVolleyApi(url);
            }
        });
    }


    private void networkCallUsingVolleyApi(String url) {
        if (AppManager.isOnline(this)) {
            showLoader(getString(R.string.loading_text));
            new VolleyGetNetworkCall(this, this, url).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private AlertDialog loaderDialog;

    private void showLoader(String loaderMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.android_loader, null);
        builder.setView(view);
        builder.create();
        loaderDialog = builder.show();
        loaderDialog.setCancelable(false);
    }

    private void closeLoader() {
        if (loaderDialog != null && loaderDialog.isShowing()) {
            loaderDialog.dismiss();
        }
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P(JSonResponse);
        closeLoader();
        try {
            String msg = "Something went wrong";
            JSONObject jsonObject = new JSONObject(JSonResponse);
            String status = jsonObject.getString("statuscode");
            if (jsonObject.has("message"))
                msg = "" + jsonObject.getString("message");
            if (REQUEST_TYPE == 1 && status.equalsIgnoreCase("TXN")) {
                mShowDialog(msg);
            } else {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
        Toast.makeText(this, "Some thing wrong", Toast.LENGTH_SHORT).show();
    }

    protected void mShowDialog(final String message) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setPositiveButton("OK", (dialog, i) -> {
            finish();
        });
        AlertDialog alert = builder1.create();
        alert.setCancelable(false);
        alert.show();
    }

}
