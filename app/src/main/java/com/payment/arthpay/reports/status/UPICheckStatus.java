package com.payment.arthpay.reports.status;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.httpRequest.VolleyPostNetworkCall;
import com.payment.arthpay.utill.AppHandler;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;

import java.util.HashMap;
import java.util.Map;

public class UPICheckStatus extends AppCompatActivity implements VolleyPostNetworkCall.RequestResponseLis {
    Activity context;
    String txnId;
    ProgressDialog dialog;
    private LottieAnimationView lottie;
    private TextView noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aeps_matm_activity_handler);
        lottie = findViewById(R.id.lottie);
        noData = findViewById(R.id.noData);
        context = UPICheckStatus.this;
        txnId = getIntent().getStringExtra("txnId");
        networkCallUsingVolleyApi(Constents.URL.REPORT_CHECK_STATUS, false);
    }

    AlertDialog alert;
    private void popUp(String title, String txn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(txn)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                    dialog.dismiss();
                    finish();
                });
        alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (alert != null && alert.isShowing()) {
            alert.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (alert != null && alert.isShowing()) {
            alert.dismiss();
        }
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            new VolleyPostNetworkCall(this, this, url, isLoad).netWorkCall(param());
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("txnid", txnId);
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {
            String status = "Status : " + AppHandler.getStatus(JSonResponse);
            String message = AppHandler.getMessage(JSonResponse);
            popUp(status, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
        lottie.setAnimation(R.raw.network_error);
        noData.setText(msg);
    }
}
