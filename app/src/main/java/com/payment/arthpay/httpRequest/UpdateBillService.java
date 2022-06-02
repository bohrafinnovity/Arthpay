package com.payment.arthpay.httpRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.payment.arthpay.R;
import com.payment.arthpay.app.AppController;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONObject;

public class UpdateBillService {

    private Activity context;
    private TextView tvWallet;
    private TextView tvAeps;
    private TextView tvMicro;
    private OnComplete listener;

    public UpdateBillService(Activity context, TextView tvWallet, TextView tvAeps, TextView tvMicro) {
        this.context = context;
        this.tvWallet = tvWallet;
        this.tvAeps = tvAeps;
        this.tvMicro = tvMicro;
        netWorkCall();
    }

    public UpdateBillService(Activity context, TextView tvWallet, TextView tvAeps) {
        this.context = context;
        this.tvWallet = tvWallet;
        this.tvAeps = tvAeps;
        netWorkCall();
    }

    public UpdateBillService(Activity context, TextView tvWallet) {
        this.context = context;
        this.tvWallet = tvWallet;
        netWorkCall();
    }

    public UpdateBillService(Activity context) {
        this.context = context;
        netWorkCall();
    }

    public UpdateBillService(Activity context, OnComplete listener) {
        this.context = context;
        this.listener = listener;
        netWorkCall();
    }

    private void netWorkCall() {
        if (AppManager.isOnline(context)) {
            if (listener == null)
                showLoader();
            netVolleyWorkCall();
        } else {
            Toast.makeText(context, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private ProgressDialog loaderDialog;

    private void showLoader() {
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
//        LayoutInflater layoutInflater = context.getLayoutInflater();
//        View view = layoutInflater.inflate(R.layout.android_loader, null);
//        builder.setView(view);
//        builder.create();
//        loaderDialog = builder.show();
//        loaderDialog.setCancelable(false);

        loaderDialog = new ProgressDialog(context);
        loaderDialog.setMessage("Please wait...");
        loaderDialog.setCancelable(false);
        loaderDialog.show();
    }

    private void closeLoader() {
        if (!context.isDestroyed())
            if (loaderDialog != null && loaderDialog.isShowing()) {
                loaderDialog.dismiss();
            }
    }

    private void netVolleyWorkCall() {
        String token = SharedPrefs.getValue(context, SharedPrefs.APP_TOKEN);
        String id = SharedPrefs.getValue(context, SharedPrefs.USER_ID);

        String fToken = "";
        String uid = SharedPrefs.getValue(context, SharedPrefs.FIREBASE_TOKEN);
        if (uid != null && uid.length() > 0)
            fToken = uid;
        Print.P("uid "+  uid);
        String apiUrl = Constents.URL.baseUrl + "api/android/getbalance?apptoken=" + token + "&user_id=" + id+
                "&uid=" + fToken +
                "&device_id=" + Constents.MOBILE_ID;

        Print.P("url "+  apiUrl);
        StringRequest sendRequest = new StringRequest(
                Request.Method.GET,
                apiUrl,
                result -> {
                    closeLoader();
                    Print.P("Volley Form Response : " + result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("UA")) {
                            AppManager.getInstance().logoutApp(context);
                        } else if (status.equalsIgnoreCase("txn")) {
                            JSONObject userObject = new JSONObject(jsonObject.getString("data"));
                            if (userObject.has("mainwallet"))
                                SharedPrefs.setValue(context, SharedPrefs.MAIN_WALLET, userObject.getString("mainwallet"));
                            else
                                SharedPrefs.setValue(context, SharedPrefs.MAIN_WALLET, userObject.getString("balance"));

                            if (userObject.has("microatmbalance")) {
                                SharedPrefs.setValue(context, SharedPrefs.MICRO_ATM_BALANCE, userObject.getString("microatmbalance"));
                            } else {
                                SharedPrefs.setValue(context, SharedPrefs.MICRO_ATM_BALANCE, "NO");
                            }

                            SharedPrefs.setValue(context, SharedPrefs.APES_BALANCE, userObject.getString("aepsbalance"));
                            String walletAmount = context.getString(R.string.rupee) + " " + SharedPrefs.getValue(context, SharedPrefs.MAIN_WALLET);
                            String aepsAmount = context.getString(R.string.rupee) + " " + SharedPrefs.getValue(context, SharedPrefs.APES_BALANCE);
                            String MaMOUNT = context.getString(R.string.rupee) + " " + SharedPrefs.getValue(context, SharedPrefs.MICRO_ATM_BALANCE);

                            if (tvWallet != null)
                                tvWallet.setText(walletAmount);
                            if (tvAeps != null)
                                tvAeps.setText(aepsAmount);
                            if (tvMicro != null && !MaMOUNT.contains("NO"))
                                tvMicro.setText(MaMOUNT);

                            if (userObject.has("news")) {
                                SharedPrefs.setValue(context, SharedPrefs.NEWS, userObject.getString("news"));
                            }
                        } else {
                            Toast.makeText(context, "Balance Update Error", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (listener != null)
                        listener.onComplete();

                }
                , volleyError -> {
            closeLoader();
            volleyError.printStackTrace();
            Print.P("Not able to connect with server");
        });

        sendRequest.setRetryPolicy(new
                DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(sendRequest);
    }


    public interface OnComplete {
        void onComplete();
    }
}
