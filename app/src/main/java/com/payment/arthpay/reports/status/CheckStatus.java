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
import com.payment.arthpay.reports.MyUtil;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CheckStatus extends
        AppCompatActivity implements VolleyPostNetworkCall.RequestResponseLis {
    Activity context;
    String typeValue, id, txnId, url;
    ProgressDialog dialog;
    private LottieAnimationView lottie;
    private TextView noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aeps_matm_activity_handler);
        lottie = findViewById(R.id.lottie);
        noData = findViewById(R.id.noData);

        context = CheckStatus.this;
        typeValue = getIntent().getStringExtra("typeValue");
        txnId = getIntent().getStringExtra("txnId");
        id = getIntent().getStringExtra("id");
        url = getIntent().getStringExtra("url");
        networkCallUsingVolleyApi(url, false);
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
            new VolleyPostNetworkCall(this, this, url).netWorkCall(param());
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        if (MyUtil.isNN(id))
            map.put("id", id);
        if (MyUtil.isNN(txnId))
            map.put("txnid", txnId);
        if (MyUtil.isNN(txnId))
            map.put("txnId", txnId);
        map.put("type", typeValue);
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            String txnStatus, refNo;

            if (jsonObject.has("txn_status")) {
                Constents.IS_RELOAD_REQUEST = true;
                txnStatus = "Txn Status : " + jsonObject.getString("txn_status");
            } else {
                txnStatus = "Unknown";
            }

            if (jsonObject.has("refno")) {
                refNo = "Operator Ref No. - " + jsonObject.getString("refno");
            } else {
                refNo = "Status Not Found, Please contact admin support";
            }
            popUp(txnStatus, refNo);
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
