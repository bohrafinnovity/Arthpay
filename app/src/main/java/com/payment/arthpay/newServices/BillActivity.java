package com.payment.arthpay.newServices;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.payment.arthpay.R;
import com.payment.arthpay.activity.TransactionReciept;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.httpRequest.DemoVolleyNetworkCall;
import com.payment.arthpay.httpRequest.UpdateBillService;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.FiscalDate;
import com.payment.arthpay.utill.Print;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class BillActivity extends AppCompatActivity implements DemoVolleyNetworkCall.RequestResponseLis {

    private Button btn1, btn;
    private ImageView imgKyc;
    private ImageView imgContact, btnBack;
    private Toolbar toolbar;

    TextView tvLable, text1;

    private String amount, number, provide_id = "", selected_due_date = "", operator_name = "", transaction_type = "",
            biller_name = "", mobile_number = "", bu = "";
    private int REQUEST_TYPE = 0;
    private String TransactionId = "";
    private String providerType = "", providerName, providerID;
    private TextView tvTitle;


    private EditText etBillNumber;
    private EditText etMobileNumber;
    LinearLayout layoutContact, layoutName, layoutAmount, layoutDate, layoutBillNumber, layoutContactNumber;
    TextView tvContactNumber;
    EditText etCustomerName, etDueAmount, etDueDate;
    Map map;
    String apiUrl;

    String backgrundCallUrl = "";

    private void init() {
        toolbar = findViewById(R.id.toolbar);

        map = new HashMap();

        //Setting field lable and Description
        text1 = findViewById(R.id.text1);
        tvLable = findViewById(R.id.tvLable);
        String lable = getIntent().getStringExtra("hint");
        String desc = getIntent().getStringExtra("descInput");
        text1.setText(desc);
        tvLable.setText(lable);

        etMobileNumber = findViewById(R.id.etMobileNumber);
        etBillNumber = findViewById(R.id.etBillNumber);
        layoutContact = findViewById(R.id.layoutContact);
        layoutBillNumber = findViewById(R.id.layoutBillNumber);
        layoutContactNumber = findViewById(R.id.layoutContactNumber);
        layoutName = findViewById(R.id.layoutName);
        layoutAmount = findViewById(R.id.layoutAmount);
        layoutDate = findViewById(R.id.layoutDate);
        tvContactNumber = findViewById(R.id.tvContactNumber);
        etCustomerName = findViewById(R.id.etCustomerName);
        etDueAmount = findViewById(R.id.etDueAmount);
        etDueDate = findViewById(R.id.etDueDate);

        btn = findViewById(R.id.btn);
        btn1 = findViewById(R.id.btn1);

        tvTitle = findViewById(R.id.tvTitle);
        btnBack = findViewById(R.id.btnBack);
        operator_name = getIntent().getStringExtra("name");
        provide_id = getIntent().getStringExtra("id");
        tvTitle.setText(operator_name);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_bill);

        init();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    amount = etDueAmount.getText().toString();
                    number = etBillNumber.getText().toString();
                    biller_name = etCustomerName.getText().toString();
                    mobile_number = etMobileNumber.getText().toString();
                    selected_due_date = etDueDate.getText().toString();
                    mShowDialog(getString(R.string.refund_alert));
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidVerify()) {
                    amount = etDueAmount.getText().toString();
                    number = etBillNumber.getText().toString();
                    biller_name = etCustomerName.getText().toString();
                    mobile_number = etMobileNumber.getText().toString();
                    selected_due_date = etDueDate.getText().toString();
                    transaction_type = "validate";
                    apiUrl = Constents.URL.baseUrl + "api/android/billpay/transaction?apptoken=" +
                            SharedPrefs.getValue(BillActivity.this, SharedPrefs.APP_TOKEN) + "&user_id=" +
                            SharedPrefs.getValue(BillActivity.this, SharedPrefs.USER_ID) + "&type="
                            + transaction_type + "&provider_id=" + provide_id + "&number=" + number + "&mobile=" + mobile_number +
                            "&amount=" + amount + "&biller=" + biller_name + "&duedate=" + selected_due_date + "&bu=" + bu;

                    REQUEST_TYPE = 1;
                    networkCallUsingVolleyApi(apiUrl);
                    //prepareLayout();
                }
            }
        });

        String fDate = FiscalDate.getFinancialDate(Calendar.getInstance());
        etDueDate.setText(fDate);
        etDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final AlertDialog.Builder adb = new AlertDialog.Builder(
                        BillActivity.this);
                final View view = LayoutInflater.from(BillActivity.this).inflate(R.layout.date_picker, null);
                adb.setView(view);
                final Dialog dialog;
                adb.setPositiveButton(R.string.add_dialog,
                        (dialog1, arg1) -> {
                            DatePicker etDatePicker = view.findViewById(R.id.datePicker1);
                            //etDatePicker.setMinDate(System.currentTimeMillis() - 1000);
                            Calendar cal = GregorianCalendar.getInstance();
                            cal.set(etDatePicker.getYear(), etDatePicker.getMonth(), etDatePicker.getDayOfMonth());
                            Date date = null;
                            date = cal.getTime();
                            String approxDateString = Constents.SHOWING_DATE_FORMAT.format(date);
                            etDueDate.setText(approxDateString);
                        });
                dialog = adb.create();
                dialog.show();
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


    private void prepareLayout() {

        layoutContactNumber.setVisibility(View.GONE);
        layoutBillNumber.setVisibility(View.GONE);
        btn1.setVisibility(View.GONE);

        Animation animate = AnimationUtils.loadAnimation(BillActivity.this, R.anim.right_to_left);
        animate.setDuration(500);
        animate.setFillAfter(true);
        layoutContact.startAnimation(animate);
        layoutContact.setVisibility(View.VISIBLE);
        layoutName.setVisibility(View.VISIBLE);
        layoutAmount.setVisibility(View.VISIBLE);
        layoutDate.setVisibility(View.VISIBLE);

        tvContactNumber.setText(etMobileNumber.getText().toString());

        Animation animate1 = AnimationUtils.loadAnimation(BillActivity.this, R.anim.right_to_left);
        animate.setDuration(500);
        animate.setFillAfter(true);
        btn.startAnimation(animate1);
        btn.setVisibility(View.VISIBLE);

    }

    private boolean isValidVerify() {

        if (etBillNumber.getText() == null || etBillNumber.getText().length() == 0) {
            Toast.makeText(this, R.string.enter_bill_number, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (provide_id == null || provide_id.length() == 0) {
            Toast.makeText(this, R.string.invalid_partner, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etMobileNumber.getText() == null || etMobileNumber.getText().length() == 0) {
            Toast.makeText(this, R.string.enter_mobile_number, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValid() {

        if (etBillNumber.getText() == null || etBillNumber.getText().length() == 0) {
            Toast.makeText(this, R.string.enter_bill_number, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (provide_id == null || provide_id.length() == 0) {
            Toast.makeText(this, R.string.select_provider, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etMobileNumber.getText() == null || etMobileNumber.getText().length() == 0) {
            Toast.makeText(this, R.string.enter_mobile_number, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etCustomerName.getText() == null || etCustomerName.getText().length() == 0) {
            Toast.makeText(this, R.string.enter_biller_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        String amount = etDueAmount.getText().toString();
        if (etDueAmount.getText() == null || etDueAmount.getText().length() == 0) {
            Toast.makeText(this, R.string.enter_bill_amount, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            switch (requestCode) {
//                case 100:
////                    provide_id = data.getStringExtra("id");
////                    operator_name = data.getStringExtra("name");
////                    etOperator.setText(operator_name);
//                    break;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    protected void mShowDialog(final String message) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(BillActivity.this);
        builder1.setMessage("Recharge of " + operator_name + "\nAmount - \u20B9 " + amount + "\nNote - " + message).setTitle(R.string.recharge_confirmation);
        builder1.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();

                String type = "payment";
                String url = Constents.URL.baseUrl + "api/android/billpay/transaction?apptoken=" + SharedPrefs.getValue(BillActivity.this,
                        SharedPrefs.APP_TOKEN) + "&user_id=" + SharedPrefs.getValue(BillActivity.this, SharedPrefs.USER_ID) + "&type="
                        + type + "&provider_id=" + provide_id + "&number=" + number + "&mobile=" + mobile_number + "&amount=" + amount + "&biller=" +
                        biller_name + "&duedate=" + selected_due_date + "&bu=" + bu;

                if (TransactionId != null && TransactionId.length() > 0) {
                    url = url + "&TransactionId=" + TransactionId;
                }

                REQUEST_TYPE = 0;
                networkCallUsingVolleyApi(url);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder1.create();
        alert.setCancelable(false);
        alert.show();
    }

    private void networkCallUsingVolleyApi(String url) {
        if (AppManager.isOnline(this)) {
            //showLoader(getString(R.string.loading_text));
            backgrundCallUrl = url;
            new getJSONData().execute(url);
        } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
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

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P("Res : " + JSonResponse);
        //closeLoader();
        if (REQUEST_TYPE == 1) {
            mVerifyShowResponse(JSonResponse);
        } else {
            Showdata(JSonResponse);
        }
    }

    @Override
    public void onFailRequest(String msg) {
        //closeLoader();
        Toast.makeText(this, R.string.recharge_failed, Toast.LENGTH_SHORT).show();
    }

    private void Showdata(final String json) {
        String status = "";
        String message = "";
        String txnid = "";
        String rrn = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("statuscode"))
                status = jsonObject.getString("statuscode");
            else
                status = jsonObject.getString("status");
            message = jsonObject.getString("message");
            txnid = jsonObject.getString("txnid");
            rrn = jsonObject.getString("rrn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (status.equalsIgnoreCase("TXN")) {
            Intent intent = new Intent(BillActivity.this, TransactionReciept.class);
            intent.putExtra("status", status);
            intent.putExtra("message", message);
            intent.putExtra("transactionid", txnid);
            intent.putExtra("transactionrrn", rrn);
            intent.putExtra("transaction_type", "Bill Pay");
            intent.putExtra("operator", operator_name);
            intent.putExtra("number", number);
            intent.putExtra("price", amount);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    protected void mVerifyShowResponse(final String json) {
        String status = "", message = "";
        prepareLayout();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("statuscode")) {
                status = jsonObject.getString("statuscode");
            }

            if (jsonObject.has("status")) {
                status = jsonObject.getString("status");
            }

            if (jsonObject.has("message")) {
                message = jsonObject.getString("message");
            }

            if (status.equalsIgnoreCase("txn")) {
                if (jsonObject.has("data") && !jsonObject.getString("data").equals("")) {

                    JSONObject data = jsonObject.getJSONObject("data");
                    biller_name = data.getString("customername");
                    amount = data.getString("dueamount");
                    selected_due_date = data.getString("duedate");
                    if (data.has("TransactionId")) {
                        TransactionId = data.getString("TransactionId");
                    }
                    etDueAmount.setText(amount);
                    etCustomerName.setText(biller_name);
                    etDueDate.setText(selected_due_date);
                }

                Toast.makeText(this, R.string.bill_fetched, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    ProgressDialog dialog;

    class getJSONData extends AsyncTask<String, String, String> {


        HttpURLConnection urlConnection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BillActivity.this);
            dialog.setMessage(getString(R.string.please_wait));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(backgrundCallUrl);
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
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            //System.out.print(" response : " + result);
            //Log.e("data", result);

            if (!result.equals("")) {
                if (REQUEST_TYPE == 1) {
                    mVerifyShowResponse(result);
                } else {
                    Showdata(result);
                }
            } else {
                Toast.makeText(BillActivity.this, "Not getting any response from server", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
