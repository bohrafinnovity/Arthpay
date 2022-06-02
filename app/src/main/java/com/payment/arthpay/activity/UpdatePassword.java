package com.payment.arthpay.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.httpRequest.VolleyGetNetworkCall;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;


public class UpdatePassword extends AppCompatActivity implements VolleyGetNetworkCall.RequestResponseLis {

    private TextInputEditText etNewPassword;
    private TextInputEditText etConfirmPass;
    private TextInputEditText etOldPassword;
    private Button btnUpdate;
    private String contact;
    private int REQUEST_TYPE = 0;
    private String activityFlag = null;

    public void init() {
        etNewPassword = (TextInputEditText) findViewById(R.id.etNewPassword);
        etConfirmPass = (TextInputEditText) findViewById(R.id.etConfirmPass);
        etOldPassword = (TextInputEditText) findViewById(R.id.etOldPassword);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        init();

        activityFlag = getIntent().getStringExtra("activityFlag");

        if (activityFlag != null) {
            findViewById(R.id.oldCon).setVisibility(View.GONE);
        } else {
            findViewById(R.id.oldCon).setVisibility(View.VISIBLE);
        }

        btnUpdate.setOnClickListener(v -> {
            if (isValid()) {
                String url = "";
                if (activityFlag == null) {
                    String oldPass = etOldPassword.getText().toString();
                    String newPass = etNewPassword.getText().toString();
                    contact = SharedPrefs.getValue(UpdatePassword.this, SharedPrefs.USER_CONTACT);
                    url = Constents.URL.baseUrl + "api/android/auth/password/change?apptoken=" +
                            SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN) +
                            "&user_id=" + SharedPrefs.getValue(this, SharedPrefs.USER_ID) +
                            "&oldpassword=" + oldPass + "&password=" + newPass;
                    // Print.P(url);
                } else {
                    contact = getIntent().getStringExtra("contact");
                    url = Constents.URL.baseUrl + "api/android/auth/reset/request?mobile=" + contact;
                    //Print.P(url);
                }

                REQUEST_TYPE = 0;
                networkCallUsingVolleyApi(url);
            }
        });

    }

    private boolean isValid() {

        if (activityFlag == null) {
            if (etOldPassword.getText() == null || etOldPassword.getText().toString().length() == 0) {
                Toast.makeText(this, "Old password is required", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!SharedPrefs.getValue(this, SharedPrefs.PASSWORD).equals(etOldPassword.getText().toString())) {
                Toast.makeText(this, "Old password is not correct", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (etNewPassword.getText() == null || etNewPassword.getText().toString().length() == 0) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etNewPassword.getText().toString().length() < 8) {
            Toast.makeText(this, "Password length should be greater or equal to 8", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etConfirmPass.getText() == null || etConfirmPass.getText().toString().length() == 0) {
            Toast.makeText(this, "Confirm password is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!etNewPassword.getText().toString().equals(etConfirmPass.getText().toString())) {
            Toast.makeText(this, "Both Password should be same", Toast.LENGTH_SHORT).show();
            etConfirmPass.setText("");
            return false;
        }

        return true;
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

    private void showOtpPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_otp_dialog, null);
        Button v = customView.findViewById(R.id.otp_submit);
        final EditText e = customView.findViewById(R.id.otp);

        builder.setView(customView);
        builder.create();
        final AlertDialog alertDialog = builder.show();
        v.setOnClickListener(v1 -> {
            String otp = e.getText().toString();
            alertDialog.dismiss();
            String url = new String(Constents.URL.baseUrl + "api/android/auth/reset?mobile=" +
                    contact + "&token=" + otp + "&password=" + etNewPassword.getText().toString());
            REQUEST_TYPE = 1;
            networkCallUsingVolleyApi(url);
        });
    }

    private void successPopUp(String msg) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(msg);
        builder1.setCancelable(false)
                .setTitle("Success")
                .setPositiveButton("Ok", (dialog, i) -> {
                    dialog.dismiss();
//                    new PrefLoginManager(UpdatePassword.this).setFarmerLoginRes("");
//                    SharedPrefs.clearAllPreferences(UpdatePassword.this);
//                    startActivity(new Intent(UpdatePassword.this, Login.class));
//                    finishAffinity();
                    AppManager.getInstance().logoutApp(UpdatePassword.this);
                });
        AlertDialog alert = builder1.create();
        alert.setCancelable(false);
        alert.show();
    }


    @Override
    public void onSuccessRequest(String result) {
        closeLoader();
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
                    if (REQUEST_TYPE == 0 && activityFlag == null) {
                        successPopUp(message);
                    } else if (REQUEST_TYPE == 0) {
                        showOtpPopUp();
                    } else {
                        successPopUp(message);
                    }
                } else {
                    Toast.makeText(UpdatePassword.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(UpdatePassword.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
