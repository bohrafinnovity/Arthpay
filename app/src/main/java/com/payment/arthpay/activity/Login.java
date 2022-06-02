package com.payment.arthpay.activity;

import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.payment.arthpay.MainActivity;
import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.httpRequest.VolleyGetNetworkCall;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.PrefLoginManager;
import com.payment.arthpay.utill.Print;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity implements VolleyGetNetworkCall.RequestResponseLis {

    private Button loginButton;
    private EditText etUsername, etPassword;
    private Context context;
    private TextView tvForgetPassword;
    private int REQUEST_TYPE = 0;
    ProgressDialog dialog;
    AlertDialog alertDialog = null;
    String username = "";
    String password = "";
    TextView signUp;

    private void init() {
        context = Login.this;
        loginButton = findViewById(R.id.loginButton);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        signUp = findViewById(R.id.signUp);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        init();

        loginButton.setOnClickListener(v -> {
            if (isValid(false)) {
                REQUEST_TYPE = 1;
                String fToken = "";
                String uid = SharedPrefs.getValue(this, SharedPrefs.FIREBASE_TOKEN);
                if (uid != null && uid.length() > 0)
                    fToken = uid;
                Print.P("uid "+  uid);
                String url = Constents.URL.baseUrl + "api/android/auth?mobile=" + etUsername.getText().
                        toString() +
                        "&password=" + etPassword.getText().toString() +
                        "&uid=" + fToken +
                        "&device_id=" + Constents.MOBILE_ID;
                Print.P(url);
                networkCallUsingVolleyApi(param(), url);
            }
        });

        tvForgetPassword.setOnClickListener(v -> {
            if (AppManager.isOnline(Login.this)) {
                mShowPhoneDialog();
            } else {
                Toast.makeText(Login.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        signUp.setOnClickListener(view -> startActivity(new Intent(Login.this, Signup.class)));


    }


    private void networkCallUsingVolleyApi(Map<String, String> map, String url) {
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

    public Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        //System.out.print("JSON Res Param: " + new JSONObject(map));
        return map;
    }

    private boolean isValid(boolean isForget) {
        if (etUsername.getText().toString().length() == 0) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etUsername.getText().toString().length() != 10) {
            Toast.makeText(this, "Invalid contact number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isForget) {
            if (etPassword.getText().toString().length() == 0) {
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P("Log Res" + JSonResponse);
        closeLoader();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSonResponse);
            if (jsonObject.has("status")) {
                if (REQUEST_TYPE == 2) {
                    if (jsonObject.has("message")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String status = jsonObject.getString("status");
                    //String status = "OTP";
                    if (status.equals("TXN")) {
                        JSONObject userObject = new JSONObject(jsonObject.getString("userdata"));
                        SharedPrefs.setValue(context, SharedPrefs.USER_ID, userObject.getString("id"));
                        SharedPrefs.setValue(context, SharedPrefs.USER_NAME, userObject.getString("name"));
                        SharedPrefs.setValue(context, SharedPrefs.USER_EMAIL, userObject.getString("email"));
                        SharedPrefs.setValue(context, SharedPrefs.USER_CONTACT, userObject.getString("mobile"));
                        SharedPrefs.setValue(context, SharedPrefs.SUPPORT_ADDRESS, userObject.getString("address"));

                        if (userObject.has("supportemail"))
                            SharedPrefs.setValue(context, SharedPrefs.SUPPORT_EMAIL, userObject.getString("supportemail"));
                        else
                            SharedPrefs.setValue(context, SharedPrefs.SUPPORT_EMAIL, "Not Available");

                        if (userObject.has("supportnumber"))
                            SharedPrefs.setValue(context, SharedPrefs.SUPPORT_NUMBER, userObject.getString("supportnumber"));
                        else
                            SharedPrefs.setValue(context, SharedPrefs.SUPPORT_NUMBER, "Not Available");
                        if (userObject.has("otpverify"))
                            SharedPrefs.setValue(context, SharedPrefs.OTP_VERIFY, userObject.getString("otpverify"));

                        if (userObject.has("mainwallet"))
                            SharedPrefs.setValue(context, SharedPrefs.MAIN_WALLET, userObject.getString("mainwallet"));
                        else
                            SharedPrefs.setValue(context, SharedPrefs.MAIN_WALLET, userObject.getString("balance"));

                        if (userObject.has("nsdlwallet")) {
                            SharedPrefs.setValue(context, SharedPrefs.NSDL_WALLET, userObject.getString("nsdlwallet"));
                        } else {
                            SharedPrefs.setValue(context, SharedPrefs.NSDL_WALLET, userObject.getString("nsdlbalance"));
                        }

                        if (userObject.has("status")) {
                            SharedPrefs.setValue(context, SharedPrefs.STATUS, userObject.getString("status"));
                        } else {
                            SharedPrefs.setValue(context, SharedPrefs.STATE, userObject.getString("status_id"));
                        }
                        if (userObject.has("microatmbalance")) {
                            SharedPrefs.setValue(context, SharedPrefs.MICRO_ATM_BALANCE, userObject.getString("microatmbalance"));
                        } else {
                            SharedPrefs.setValue(context, SharedPrefs.MICRO_ATM_BALANCE, "NO");
                        }

                        if (userObject.has("tokenamount")) {
                            SharedPrefs.setValue(context, SharedPrefs.TOKEN_AMOUNT, userObject.getString("tokenamount"));
                        }

                        if (userObject.has("utiid")) {
                            SharedPrefs.setValue(context, SharedPrefs.UTI_ID, userObject.getString("utiid"));
                        }
                        if (userObject.has("slides")) {
                            try {
                                JSONArray banners = userObject.getJSONArray("slides");
                                String bannerarray = banners.toString();
                                SharedPrefs.setValue(context, SharedPrefs.BANNERARRAY, bannerarray);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (userObject.has("news"))
                            SharedPrefs.setValue(context, SharedPrefs.NEWS, userObject.getString("news"));

                        SharedPrefs.setValue(context, SharedPrefs.STATE, userObject.getString("state"));
                        SharedPrefs.setValue(context, SharedPrefs.APES_BALANCE, userObject.getString("aepsbalance"));
                        SharedPrefs.setValue(context, SharedPrefs.LOCKED_AMOUNT, userObject.getString("lockedamount"));
                        SharedPrefs.setValue(context, SharedPrefs.ROLE_ID, userObject.getString("role_id"));
                        SharedPrefs.setValue(context, SharedPrefs.PARENT_ID, userObject.getString("parent_id"));
                        SharedPrefs.setValue(context, SharedPrefs.COMPANY_ID, userObject.getString("scheme_id"));

                        SharedPrefs.setValue(context, SharedPrefs.ADDRESS, userObject.getString("address"));
                        SharedPrefs.setValue(context, SharedPrefs.SHOP_NAME, userObject.getString("shopname"));
                        SharedPrefs.setValue(context, SharedPrefs.GSTIN, userObject.getString("gstin"));
                        SharedPrefs.setValue(context, SharedPrefs.CITY, userObject.getString("city"));

                        SharedPrefs.setValue(context, SharedPrefs.PINCODE, userObject.getString("pincode"));
                        SharedPrefs.setValue(context, SharedPrefs.PANCARD, userObject.getString("pancard"));
                        SharedPrefs.setValue(context, SharedPrefs.AADHAR_CARD, userObject.getString("aadharcard"));
                        SharedPrefs.setValue(context, SharedPrefs.KYC, userObject.getString("kyc"));

                        SharedPrefs.setValue(context, SharedPrefs.ACCOUNT, userObject.getString("account"));
                        SharedPrefs.setValue(context, SharedPrefs.BANK, userObject.getString("bank"));
                        SharedPrefs.setValue(context, SharedPrefs.IFSC, userObject.getString("ifsc"));

                        if (userObject.has("account2")) {
                            SharedPrefs.setValue(context, SharedPrefs.ACCOUNT2, userObject.getString("account2"));
                            SharedPrefs.setValue(context, SharedPrefs.BANK2, userObject.getString("bank2"));
                            SharedPrefs.setValue(context, SharedPrefs.IFSC2, userObject.getString("ifsc2"));
                        }

                        if (userObject.has("account3")) {
                            SharedPrefs.setValue(context, SharedPrefs.ACCOUNT3, userObject.getString("account3"));
                            SharedPrefs.setValue(context, SharedPrefs.BANK3, userObject.getString("bank3"));
                            SharedPrefs.setValue(context, SharedPrefs.IFSC3, userObject.getString("ifsc3"));
                        }

                        SharedPrefs.setValue(context, SharedPrefs.APP_TOKEN, userObject.getString("apptoken"));

                        // Role Object
                        JSONObject roleObject = new JSONObject(userObject.getString("role"));
                        SharedPrefs.setValue(context, SharedPrefs.ROLE_NAME, roleObject.getString("name"));
                        SharedPrefs.setValue(context, SharedPrefs.ROLE_SLUG, roleObject.getString("slug"));

                        // Company Object
                        JSONObject companyObject = new JSONObject(userObject.getString("company"));
                        SharedPrefs.setValue(context, SharedPrefs.COMPANY_ID, companyObject.getString("id"));
                        SharedPrefs.setValue(context, SharedPrefs.COMPANY_NAME, companyObject.getString("companyname"));
                        SharedPrefs.setValue(context, SharedPrefs.WEBSITE, companyObject.getString("website"));
                        SharedPrefs.setValue(context, SharedPrefs.LOGO, companyObject.getString("logo"));
                        SharedPrefs.setValue(context, SharedPrefs.LOGIN_ID, etUsername.getText().toString());
                        SharedPrefs.setValue(context, SharedPrefs.PASSWORD, etPassword.getText().toString());

                        new PrefLoginManager(context).setFarmerLoginRes("1");
                        startActivity(new Intent(context, MainActivity.class));
                        finishAffinity();
                    } else if (status.equalsIgnoreCase("OTP")) {
                        if (jsonObject.has("message")) {
                            String msg = jsonObject.getString("message");
                            showOtpPopUp(msg);
                        }
                    } else if (status.equalsIgnoreCase("ERR")) {
                        if (jsonObject.has("message")) {
                            String msg = jsonObject.getString("message");
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Print.P("Json Parser Exception");
            closeLoader();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
    }

    private void showOtpPopUp(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_login_otp_dialog, null);
        Button btnSubmit = customView.findViewById(R.id.otp_submit);
        Button btnResend = customView.findViewById(R.id.btnResend);
        final EditText etOtp = customView.findViewById(R.id.otp);
        final TextView tvMsg = customView.findViewById(R.id.tvMsg);
        final ImageView imgClose = customView.findViewById(R.id.imgClose);
        tvMsg.setText(msg);
        builder.setView(customView);
        builder.create();
        final AlertDialog alertDialog = builder.show();
        builder.setCancelable(false);
        btnSubmit.setOnClickListener(v1 -> {
            String otp = etOtp.getText().toString();
            Print.P(otp);
            if (otp.length() > 0) {
                REQUEST_TYPE = 1;
                String url = Constents.URL.baseUrl + "api/android/auth?mobile=" + etUsername.getText().
                        toString() + "&password=" + etPassword.getText().toString() +
                        "&otp=" + otp + "&device_id=" + Constents.MOBILE_ID;
                networkCallUsingVolleyApi(param(), url);
                alertDialog.dismiss();
            } else {
                Toast.makeText(context, "Please input otp for login", Toast.LENGTH_SHORT).show();
            }
        });

        btnResend.setOnClickListener(v -> {
            REQUEST_TYPE = 2;
            String url = Constents.URL.baseUrl + "api/android/auth?mobile=" + etUsername.getText().
                    toString() + "&password=" + etPassword.getText().toString() +
                    "&otp=resend" + "&device_id=" + Constents.MOBILE_ID;
            networkCallUsingVolleyApi(param(), url);
            Toast.makeText(context, "OTP resend successfully..", Toast.LENGTH_SHORT).show();
        });

        imgClose.setOnClickListener(v -> alertDialog.dismiss());
    }

    private void mForgotPassword(final String username) {

        class getJSONData extends AsyncTask<String, String, String> {

            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(Login.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(Constents.URL.baseUrl + "api/android/auth/reset/request?mobile=" + username + "&device_id=" + Constents.MOBILE_ID);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }


                return result.toString();
            }

            @Override
            protected void onPostExecute(String result) {

                //Do something with the JSON string
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                String myresult = "";
                myresult = result;
                if (!myresult.equals("")) {
                    String status = "";
                    String message = "";
                    try {
                        JSONObject jsonObject = new JSONObject(myresult);
                        if (jsonObject.has("status")) {
                            status = jsonObject.getString("status");
                        }

                        if (jsonObject.has("message")) {
                            message = jsonObject.getString("message");
                        }

                        if (status.equalsIgnoreCase("txn")) {
                            mShowOTPDialog();
                        } else {
                            Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        }

        getJSONData getJSONData = new getJSONData();
        getJSONData.execute();
    }

    protected void mShowOTPDialog() {
        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2 = inflater2.inflate(R.layout.custome_alert_dialog_for_enter_otp, null);

        final EditText edittext_otp = v2.findViewById(R.id.edittext_otp);
        final EditText edittext_new_password = v2.findViewById(R.id.edittext_new_password);
        TextView textview_resend_otp = v2.findViewById(R.id.textview_resend_otp);
        Button button_submit = v2.findViewById(R.id.button_submit);
        Button button_cancel = v2.findViewById(R.id.button_cancel);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppManager.isOnline(Login.this)) {
                    if (edittext_otp.getText().toString().equals("")) {
                        Toast.makeText(Login.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                    } else if (edittext_new_password.getText().toString().equals("")) {
                        Toast.makeText(Login.this, "Please enter new password", Toast.LENGTH_SHORT).show();
                    } else {
                        String otp = edittext_otp.getText().toString();
                        String password = edittext_new_password.getText().toString();
                        mSubmitPassword(username, otp, password);
                    }
                } else {
                    Toast.makeText(Login.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textview_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppManager.isOnline(Login.this)) {

                    alertDialog.dismiss();
                    mForgotPassword(username);

                } else {
                    Toast.makeText(Login.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        final AlertDialog.Builder builder2 = new AlertDialog.Builder(Login.this);
        builder2.setCancelable(false);

        builder2.setView(v2);

        alertDialog = builder2.create();
        button_cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

        alertDialog.show();
    }

    protected void mShowPhoneDialog() {
        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2 = inflater2.inflate(R.layout.custome_alert_dialog_for_enter_otp, null);

        final EditText edittext_otp = v2.findViewById(R.id.edittext_otp);
        edittext_otp.setHint("Registered Mobile Number");
        final EditText edittext_new_password = v2.findViewById(R.id.edittext_new_password);
        edittext_new_password.setVisibility(View.GONE);
        TextView textview_resend_otp = v2.findViewById(R.id.textview_resend_otp);
        textview_resend_otp.setVisibility(View.GONE);
        Button button_submit = v2.findViewById(R.id.button_submit);
        Button button_cancel = v2.findViewById(R.id.button_cancel);


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppManager.isOnline(Login.this)) {
                    if (edittext_otp.getText().toString().equals("")) {
                        Toast.makeText(Login.this, "Mobile number is required.", Toast.LENGTH_SHORT).show();
                    } else {
                        alertDialog.dismiss();
                        username = edittext_otp.getText().toString();
                        mForgotPassword(username);
                    }
                } else {
                    Toast.makeText(Login.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(Login.this);
        builder2.setCancelable(false);

        builder2.setView(v2);

        alertDialog = builder2.create();
        button_cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

        alertDialog.show();
    }

    private void mSubmitPassword(final String username, final String otp, final String password) {
        class getJSONData extends AsyncTask<String, String, String> {
            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(Login.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(Constents.URL.baseUrl + "api/android/auth/reset?mobile=" + username + "&token=" + otp + "&password=" + password + "&device_id=" + Constents.MOBILE_ID);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }


                return result.toString();
            }

            @Override
            protected void onPostExecute(String result) {

                //Do something with the JSON string
                dialog.dismiss();


                String myresult = "";
                myresult = result;
                if (!myresult.equals("")) {
                    String status = "";
                    String message = "";
                    try {
                        JSONObject jsonObject = new JSONObject(myresult);
                        if (jsonObject.has("status")) {
                            status = jsonObject.getString("status");
                        }

                        if (jsonObject.has("message")) {
                            message = jsonObject.getString("message");
                        }

                        if (status.equalsIgnoreCase("txn")) {
                            if (alertDialog != null && alertDialog.isShowing()) {
                                alertDialog.dismiss();
                            }

                            Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        }

        getJSONData getJSONData = new getJSONData();
        getJSONData.execute();
    }
}
