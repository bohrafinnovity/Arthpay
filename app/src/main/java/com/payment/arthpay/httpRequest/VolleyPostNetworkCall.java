package com.payment.arthpay.httpRequest;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payment.arthpay.reports.MyUtil;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONObject;

import java.util.Map;

public class VolleyPostNetworkCall {

    private RequestResponseLis listener;
    private Activity context;
    private String apiUrl;
    private ProgressDialog progressDialog;
    private boolean isLoad = false;

    public VolleyPostNetworkCall(RequestResponseLis listener, Activity context, String apiUrl) {
        this.listener = listener;
        this.context = context;
        this.apiUrl = apiUrl;
    }

    public VolleyPostNetworkCall(RequestResponseLis listener, Activity context, String apiUrl, boolean isLoad) {
        this.listener = listener;
        this.context = context;
        this.apiUrl = apiUrl;
        this.isLoad = isLoad;
    }

    public void netWorkCall(final Map<String, String> map) {
        if (isLoad) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        StringRequest sendRequest = new StringRequest(Request.Method.POST,
                apiUrl,
                Response -> {
                    cL();
                    Print.P("Volley Form Response : " + Response);
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
                    }
                }
                , volleyError -> {
                    cL();
                    volleyError.printStackTrace();
                    Print.P("Not able to connect with server");
                    listener.onFailRequest("Network connection error");
                }) {
            @Override
            protected Map<String, String> getParams() {
                String t = SharedPrefs.getValue(context, SharedPrefs.APP_TOKEN);
                String u = SharedPrefs.getValue(context, SharedPrefs.USER_ID);
                if (MyUtil.isNN(t) && MyUtil.isNN(u)) {
                    map.put("apptoken", SharedPrefs.getValue(context, SharedPrefs.APP_TOKEN));
                    map.put("user_id", SharedPrefs.getValue(context, SharedPrefs.USER_ID));
                }

                Print.P("URL : " + apiUrl);
                Print.P("PARAMS: " + new JSONObject(map).toString());
                return map;
            }
        };

        sendRequest.setRetryPolicy(new
                DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(sendRequest);
    }

    public interface RequestResponseLis {
        void onSuccessRequest(String JSonResponse);

        void onFailRequest(String msg);
    }

    private void cL() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
