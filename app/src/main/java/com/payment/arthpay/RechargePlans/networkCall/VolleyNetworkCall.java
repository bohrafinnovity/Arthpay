package com.payment.arthpay.RechargePlans.networkCall;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payment.arthpay.R;
import com.payment.arthpay.utill.Print;

public class VolleyNetworkCall {

    private android.app.AlertDialog loaderDialog;
    private Activity context;


    CallResponseLis listener;

    public VolleyNetworkCall(Activity context, CallResponseLis listener) {
        this.context = context;
        this.listener = listener;
    }

    public void netWorkCall(String userId, String appToken, String operator, String number, String url) {
        showLoader();
        final String u = url + "?user_id=" + userId +
                "&apptoken=" + appToken +
                "&operator=" + operator +
                "&number=" + number;
        Print.P("Plan Url: " + u);
        StringRequest sendRequest = new StringRequest(Request.Method.GET,
                u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        closeLoader();
                        listener.onSuccess(Response);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                closeLoader();
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(sendRequest);
    }

    private void showLoader() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        LayoutInflater layoutInflater = context.getLayoutInflater();
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

    public interface CallResponseLis {
        void onSuccess(String res);
    }

}
