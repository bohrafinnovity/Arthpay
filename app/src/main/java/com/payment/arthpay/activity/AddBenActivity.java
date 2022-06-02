package com.payment.arthpay.activity;

import androidx.appcompat.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.httpRequest.VolleyGetNetworkCall;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

public class AddBenActivity extends AppCompatActivity implements VolleyGetNetworkCall.RequestResponseLis {

    private String mobileValue, nameValue, beneaccountValue, ifscValue, type = "";
    private AlertDialog alertDialog;
    private static AlertDialog alertDialog_for_bank;
    private LinearLayout ll_bank_detail;
    private Button button_add_beneficiary;
    private EditText beneaccount, benename, benemobile;
    private ProgressDialog dialog;
    private EditText etBank;
    private EditText ifscCode;
    private String benebankValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mobileValue = getIntent().getStringExtra("mobile");
        nameValue = getIntent().getStringExtra("name");

        setContentView(R.layout.add_benificiary_fragment);
        init();

        etBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddBenActivity.this, OperatorList.class);
                i.putExtra("type", "bank");
                startActivityForResult(i, 100);
            }
        });

        etBank = findViewById(R.id.etBank);
        button_add_beneficiary = findViewById(R.id.button_add_benificiary);
        ll_bank_detail = findViewById(R.id.ll_bank_detail);
        button_add_beneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBenValid()) {
                    type = "addbeneficiary";
                    beneaccountValue = beneaccount.getText().toString();
                    String url = Constents.URL.baseUrl + "api/android/edmt/transaction?apptoken=" +
                            SharedPrefs.getValue(AddBenActivity.this, SharedPrefs.APP_TOKEN) + "&user_id=" +
                            SharedPrefs.getValue(AddBenActivity.this, SharedPrefs.USER_ID) + "&type=" + type + "&mobile=" +
                            mobileValue + "&benemobile=" + benemobile.getText().toString() + "&benebank=" + benebankValue +
                            "&beneifsc=" + ifscCode.getText().toString() + "&beneaccount=" + beneaccountValue +
                            "&benename=" + benename.getText().toString() + "&name=" + nameValue+ "&device_id=" + Constents.MOBILE_ID;
                    volleyHttpRequest(url);
                }
            }
        });


        beneaccount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (beneaccount.getRight() - beneaccount.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (isValid()) {
                            beneaccountValue = beneaccount.getText().toString();
                            ifscValue = ifscCode.getText().toString();
                            String url = Constents.URL.baseUrl + "api/android/edmt/transaction?apptoken=" +
                                    SharedPrefs.getValue(AddBenActivity.this, SharedPrefs.APP_TOKEN) +
                                    "&user_id=" +
                                    SharedPrefs.getValue(AddBenActivity.this, SharedPrefs.USER_ID) + "&type=accountverification&mobile="
                                    + mobileValue + "&benemobile=" + benemobile.getText().toString() + "&benebank=" + benebankValue
                                    + "&beneifsc=" + ifscValue + "&beneaccount=" + beneaccountValue + "&name=" + nameValue
                                    + "&benename=" + benename.getText().toString()+ "&device_id=" + Constents.MOBILE_ID;
                            volleyHttpRequest(url);
                        }
                        return true;
                    }
                }

                return false;
            }
        });

    }

    private void init() {
        beneaccount = findViewById(R.id.beneaccount);
        benename = findViewById(R.id.benename);
        benemobile = findViewById(R.id.benemobile);
        ifscCode = findViewById(R.id.ifscCode);
        etBank = findViewById(R.id.etBank);
        //button_verify_account.setVisibility(View.VISIBLE);
    }

    private void volleyHttpRequest(String url) {
        if (AppManager.isOnline(AddBenActivity.this)) {
            showLoader(getString(R.string.loading_text));
            new VolleyGetNetworkCall(this, AddBenActivity.this, url).netWorkCall();
        } else {
            Toast.makeText(AddBenActivity.this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private android.app.AlertDialog loaderDialog;

    private void showLoader(String loaderMsg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddBenActivity.this);
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

    private boolean isBenValid() {
        if (benebankValue.equals("")) {
            Toast.makeText(AddBenActivity.this, "Select Bank name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (beneaccount.getText().toString().equals("")) {
            Toast.makeText(AddBenActivity.this, "Enter bank account number..", Toast.LENGTH_SHORT).show();
            return false;
        } else if (benename.getText().toString().equals("")) {
            Toast.makeText(AddBenActivity.this, "Enter beneficiary name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (benemobile.getText().toString().equals("")) {
            Toast.makeText(AddBenActivity.this, "Enter beneficiary mobile", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ifscCode.getText().toString().equals("")) {
            Toast.makeText(AddBenActivity.this, "Please enter ifsc code", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValid() {
        if (beneaccount.getText().toString().equals("")) {
            Toast.makeText(AddBenActivity.this, "Please enter account number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (benemobile.getText().toString().equals("")) {
            Toast.makeText(AddBenActivity.this, "Please enter beneficiary mobile", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ifscCode.getText().toString().equals("")) {
            Toast.makeText(AddBenActivity.this, "Please enter ifsc code", Toast.LENGTH_SHORT).show();
            return false;
        } else if (benebankValue.equals("")) {
            Toast.makeText(AddBenActivity.this, "Please select bank", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onSuccessRequest(String myresult) {
        closeLoader();
        if (type.equalsIgnoreCase("addBeneficiary")) {
            mShowAddBeneStatus(myresult);
        } else {
            if (!myresult.equals("")) {
                String status = "";
                try {
                    JSONObject jsonObject = new JSONObject(myresult);

                    if (jsonObject.has("statuscode")) {
                        status = jsonObject.getString("statuscode");
                    }

                    //String status = jsonObject.getString("statuscode");
                    if (status.equalsIgnoreCase("UA")) {
                        AppManager.getInstance().logoutApp(AddBenActivity.this);
                    } else if (status.equalsIgnoreCase("txn")) {
                        if (jsonObject.has("benename")) {
                            benename.setText(jsonObject.getString("benename"));
                        }

                        if (jsonObject.has("message")) {
                            Toast.makeText(AddBenActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } else if (!status.equals("") && !status.equals("txn")) {
                        if (jsonObject.has("message")) {
                            Toast.makeText(AddBenActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddBenActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(AddBenActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
    }

    protected void mShowAddBeneStatus(final String myJSON) {
        String status = "";
        String message = "";
        try {
            JSONObject jsonObject = new JSONObject(myJSON);

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
            AppManager.getInstance().logoutApp(AddBenActivity.this);
        }else if (status.equalsIgnoreCase("txn")) {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(AddBenActivity.this);
            builder1.setMessage(message);

            builder1.setCancelable(false)
                    .setTitle("Success")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            Constents.isBack = "2";
                            finish();
                        }
                    });
            AlertDialog alert = builder1.create();
            alert.show();
        } else {
            Toast.makeText(AddBenActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 100:
                    String id, name, bankId, ifsc;
                    id = data.getStringExtra("id");
                    name = data.getStringExtra("name");
                    bankId = data.getStringExtra("bankId");
                    ifsc = data.getStringExtra("ifsc");
                    etBank.setText(name);
                    ifscCode.setText(ifsc);
                    benebankValue = bankId;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}