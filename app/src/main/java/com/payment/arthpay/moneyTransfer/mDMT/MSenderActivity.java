package com.payment.arthpay.moneyTransfer.mDMT;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.httpRequest.UpdateBillService;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

public class MSenderActivity extends AppCompatActivity {
    EditText edittext_mobileno;
    LinearLayout ll_contain_fname_lname;
    EditText edittext_fname, edittext_register_otp;
    Button button_register_continue;

    TextView textview_balance;

    String mobile = "", type = "";
    ProgressDialog dialog;
    AlertDialog alertDialog;


    String status = "";
    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmt);

        edittext_mobileno = findViewById(R.id.etMobileNumber);
        ll_contain_fname_lname = findViewById(R.id.ll_contain_fname_lname);
        edittext_fname = findViewById(R.id.etName);
        edittext_register_otp = findViewById(R.id.etOtp);
        button_register_continue = findViewById(R.id.btnRegister);
        edittext_mobileno.setText("");

        edittext_mobileno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 9) {
                    type = "verification";
                    mobile = s.toString();
                    new CallMyRestApi().execute(Constents.URL.baseUrl + "api/android/dmt/transaction?apptoken="
                            + SharedPrefs.getValue(MSenderActivity.this, SharedPrefs.APP_TOKEN) + "&user_id=" +
                            SharedPrefs.getValue(MSenderActivity.this, SharedPrefs.USER_ID) + "&type=" + type + "&mobile=" + mobile + "&device_id=" + Constents.MOBILE_ID);
                } else {
                    ll_contain_fname_lname.setVisibility(View.GONE);
                }
            }
        });

        button_register_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppManager.isOnline(MSenderActivity.this)) {
                    if (edittext_mobileno.getText().toString().equals("")) {
                        Toast.makeText(MSenderActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                    } else if (edittext_fname.getText().toString().equals("")) {
                        Toast.makeText(MSenderActivity.this, "Please enter firstname", Toast.LENGTH_SHORT).show();
                    } else if (edittext_register_otp.getText().toString().equals("")) {
                        Toast.makeText(MSenderActivity.this, "Please enter otp", Toast.LENGTH_SHORT).show();
                    } else {
                        String first_name = edittext_fname.getText().toString();
                        String otp = edittext_register_otp.getText().toString();
                        type = "registration";
                        new CallMyRestApi().execute(Constents.URL.baseUrl + "api/android/dmt/transaction?apptoken=" +
                                SharedPrefs.getValue(MSenderActivity.this, SharedPrefs.APP_TOKEN) + "&user_id=" +
                                SharedPrefs.getValue(MSenderActivity.this, SharedPrefs.USER_ID) + "&type=" + type + "&mobile=" + mobile + "&fname=" + first_name + "&otp=" + otp + "&device_id=" + Constents.MOBILE_ID);
                    }
                } else {
                    Toast.makeText(MSenderActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView tvWallet = findViewById(R.id.tvBalance);
        TextView tvAeps = findViewById(R.id.tvAeps);
        ImageView imgSync = findViewById(R.id.imgSync);
        String balance = getString(R.string.rupee) + " " + SharedPrefs.getValue(this, SharedPrefs.MAIN_WALLET);
        String aeps = getString(R.string.rupee) + " " + SharedPrefs.getValue(this, SharedPrefs.APES_BALANCE);
        tvWallet.setText(balance);
        tvAeps.setText(aeps);
        imgSync.setOnClickListener(v -> new UpdateBillService(this, tvWallet, tvAeps));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    class CallMyRestApi extends CallRestApiForMoneyTransfer {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MSenderActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!MSenderActivity.this.isDestroyed())
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            if (type.equalsIgnoreCase("verification")) {
                mShowSenderCheckStatus(s);
            } else if (type.equalsIgnoreCase("registration")) {
                mShowRegistrationStatus(s);
            }
        }
    }

    protected void mShowSenderCheckStatus(final String json) {
        String status = "";
        String message = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("statuscode")) {
                status = jsonObject.getString("statuscode");
            } else {
                status = "ERR";
            }

            if (jsonObject.has("message")) {
                message = jsonObject.getString("message");
            } else {
                message = "Technical Error";
            }

            //String status = jsonObject.getString("statuscode");
            if (status.equalsIgnoreCase("UA")) {
                AppManager.getInstance().logoutApp(MSenderActivity.this);
            } else if (status.equalsIgnoreCase("TXN")) {
                String name = jsonObject.getString("name");
                String totallimit = jsonObject.getString("totallimit");
                String usedlimit = jsonObject.getString("usedlimit");

                Bundle bundle = new Bundle();
                Intent intent = new Intent(MSenderActivity.this, SenderDetailActivity.class);
                bundle.putString("sender_number", mobile);
                bundle.putString("name", name);
                bundle.putString("status", status);
                bundle.putString("available_limit", totallimit);
                bundle.putString("total_spend", usedlimit);
                bundle.putString("json", json);
                bundle.putInt("tab", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (status.equalsIgnoreCase("RNF")) {
                ll_contain_fname_lname.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(MSenderActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void mShowRegistrationStatus(final String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("statuscode")) {
                status = jsonObject.getString("statuscode");
            }

            if (jsonObject.has("message")) {
                message = jsonObject.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //String status = jsonObject.getString("statuscode");
        if (status.equalsIgnoreCase("UA")) {
            AppManager.getInstance().logoutApp(MSenderActivity.this);
        } else if (status.equalsIgnoreCase("txn")) {
            Toast.makeText(MSenderActivity.this, message, Toast.LENGTH_SHORT).show();
            type = "verification";
            new CallMyRestApi().execute(Constents.URL.baseUrl + "api/android/dmt/transaction?apptoken=" +
                    SharedPrefs.getValue(MSenderActivity.this, SharedPrefs.APP_TOKEN) + "&user_id=" +
                    SharedPrefs.getValue(MSenderActivity.this, SharedPrefs.USER_ID) + "&type=verification" + "&mobile=" + mobile + "&device_id=" + Constents.MOBILE_ID);
        } else {
            Toast.makeText(MSenderActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
