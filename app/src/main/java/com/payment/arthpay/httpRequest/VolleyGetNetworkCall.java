package com.payment.arthpay.httpRequest;

import android.app.Activity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.payment.arthpay.app.AppController;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;

import org.json.JSONObject;

public class VolleyGetNetworkCall {

    private RequestResponseLis listener;
    private Activity context;
    private String apiUrl;

    public VolleyGetNetworkCall(RequestResponseLis listener, Activity context) {
        this.listener = listener;
        this.context = context;
        apiUrl = Constents.URL.baseUrl;
    }

    public VolleyGetNetworkCall(RequestResponseLis listener, Activity context, String apiUrl) {
        this.listener = listener;
        this.context = context;
        this.apiUrl = apiUrl+ "&device_id=" + Constents.MOBILE_ID;
    }

    public void netWorkCall() {
        Print.P("url:  " + apiUrl);
        StringRequest sendRequest = new StringRequest(
                Request.Method.GET,
                apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        try {
                            Print.P("Volley Form Response : " + Response);
                            String status = "";
                            JSONObject jsonObject = new JSONObject(Response);
                            if (jsonObject.has("statuscode")) {
                                status = jsonObject.getString("statuscode");
                            }
                            if (status.equalsIgnoreCase("UA")) {
                                AppManager.getInstance().logoutApp(context);
                            } else {
                                listener.onSuccessRequest(Response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppManager.appendLog("Error is volley get Network call");
                            AppManager.appendLog(e);
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Print.P("Not able to connect with server");
                listener.onFailRequest("Network connection error");
            }
        });
        sendRequest.setRetryPolicy(new
                DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );
        AppController.getInstance().addToRequestQueue(sendRequest);
    }

    public interface RequestResponseLis {
        void onSuccessRequest(String JSonResponse);

        void onFailRequest(String msg);
    }


}
