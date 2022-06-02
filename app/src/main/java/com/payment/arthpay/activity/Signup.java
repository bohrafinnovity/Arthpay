package com.payment.arthpay.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.httpRequest.VolleyPostNetworkCall;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Signup extends AppCompatActivity implements VolleyPostNetworkCall.RequestResponseLis {

    private Button btnSignup;
    private EditText etName;
    private EditText etMobile;
    private EditText etEmail;
    private EditText etShop;
    private EditText etPan;
    private EditText etAadhar;
    private EditText etState;
    private EditText etCity;
    private EditText etAddress;
    private EditText etPincode;
    private Spinner spnrSlug;

    private Context context;
    ProgressDialog dialog;
    String slug = "";

    private void init() {
        context = Signup.this;
        etName = findViewById(R.id.etName);
        etPincode = findViewById(R.id.etPincode);
        spnrSlug = findViewById(R.id.spnrSlug);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        etShop = findViewById(R.id.etShop);
        etPan = findViewById(R.id.etPan);
        etAadhar = findViewById(R.id.etAadhar);
        etState = findViewById(R.id.etState);
        etCity = findViewById(R.id.etCity);
        etAddress = findViewById(R.id.etAddress);
        btnSignup = findViewById(R.id.btnSubmit);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        init();

        btnSignup.setOnClickListener(v -> {
            if (AppManager.isOnline(Signup.this)) {
                if (isValid()) {
                    String url = Constents.URL.baseUrl + "api/android/auth/user/register";
                    networkCallUsingVolleyApi(param(), url);
                }
            } else {
                Toast.makeText(Signup.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });


        ArrayList<String> slugList = new ArrayList<>();
        slugList.add("Select Slug");
        slugList.add("retailer");
        slugList.add("md");
        slugList.add("distributor");
//        slugList.add("whitelable");

        ArrayAdapter stateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, slugList);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnrSlug.setAdapter(stateAdapter);

        spnrSlug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    slug = slugList.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void networkCallUsingVolleyApi(Map<String, String> map, String url) {
        if (AppManager.isOnline(this)) {
            showLoader(getString(R.string.loading_text));
            new VolleyPostNetworkCall(this, this, url).netWorkCall(map);
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
        map.put("name", etName.getText().toString());
        map.put("mobile", etMobile.getText().toString());
        map.put("email", etEmail.getText().toString());
        map.put("shopname", etShop.getText().toString());
        map.put("pancard", etPan.getText().toString());
        map.put("aadharcard", etAadhar.getText().toString());
        map.put("state", etState.getText().toString());
        map.put("city", etCity.getText().toString());
        map.put("address", etAddress.getText().toString());
        map.put("pincode", etPincode.getText().toString());
        map.put("slug", slug);

        String paramString = new JSONObject(map).toString();
        //Log.e("JSON Res Param: " , paramString);
        return map;
    }

    private boolean isValid() {

        if (etName.getText().toString().length() == 0) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etPincode.getText().toString().length() == 0) {
            Toast.makeText(this, "Pin is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etPincode.getText().toString().length() != 6) {
            Toast.makeText(this, "Pin length should be 6", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etMobile.getText().toString().length() == 0) {
            Toast.makeText(this, "Mobile number is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etMobile.getText().toString().length() != 10) {
            Toast.makeText(this, "Invalid contact", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etEmail.getText().toString().length() == 0) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etShop.getText().toString().length() == 0) {
            Toast.makeText(this, "Shop field is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etPan.getText().toString().length() == 0) {
            Toast.makeText(this, "Pancard number is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etAadhar.getText().toString().length() == 0) {
            Toast.makeText(this, "Aadhar number is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etAadhar.getText().toString().length() != 12) {
            Toast.makeText(this, "Value should be 12 digits long", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etState.getText().toString().length() == 0) {
            Toast.makeText(this, "State is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etCity.getText().toString().length() == 0) {
            Toast.makeText(this, "City is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etAddress.getText().toString().length() == 0) {
            Toast.makeText(this, "Address is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (slug.length() == 0) {
            Toast.makeText(this, "Slug value is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        //Log.e("LOG", JSonResponse);
        //{"statuscode":"TXN","message":"Thank you for choosing, your request is successfully submitted for approval"}
        closeLoader();
        JSONObject jsonObject = null;
        try {
            String msg = "some error accour";
            jsonObject = new JSONObject(JSonResponse);
            String status = "";
            if (jsonObject.has("status")){
                status = jsonObject.getString("status");
            }else if (jsonObject.has("statuscode")){
                status = jsonObject.getString("statuscode");
            }

            if (jsonObject.has("message"))
                msg = jsonObject.getString("message");

            if (status.equalsIgnoreCase("TXN")) {
                confirmPopup(msg);
            } else {
                Toast.makeText(context, "Error : " + msg, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Print.P("Json Parser Exception");
            closeLoader();
        }
    }

    private void confirmPopup(String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
    }


}
