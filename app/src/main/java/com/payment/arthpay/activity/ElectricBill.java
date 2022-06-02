package com.payment.arthpay.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.bppsServices.OperatorListNew;
import com.payment.arthpay.httpRequest.UpdateBillService;
import com.payment.arthpay.httpRequest.VolleyGetNetworkCall;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.FiscalDate;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class ElectricBill extends AppCompatActivity implements VolleyGetNetworkCall.RequestResponseLis {

    private Button btn1, btn;
    private ImageView imgKyc;
    private EditText etOperator, etAmount;
    private ImageView imgContact;
    private Toolbar toolbar;
    private EditText etBillNumber;
    private TextInputEditText etMobileNumber;
    private TextInputEditText etBillerName;
    private TextInputEditText etDate;
    private String amount, number, provide_id = "", selected_due_date = "", operator_name = "", transaction_type = "",
            biller_name = "", mobile_number = "", bu = "";

    private int REQUEST_TYPE = 0;
    private String TransactionId = "";

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        etBillNumber =  findViewById(R.id.etBillNumber);
        etOperator = (TextInputEditText) findViewById(R.id.etOperator);
        etMobileNumber = (TextInputEditText) findViewById(R.id.etMobileNumber);
        etBillerName = (TextInputEditText) findViewById(R.id.etBillerName);
        etDate = (TextInputEditText) findViewById(R.id.etDate);
        etAmount = (TextInputEditText) findViewById(R.id.etAmount);
        btn = (Button) findViewById(R.id.btn);
        btn1 = (Button) findViewById(R.id.btn1);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_bill);

        init();

        etOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ElectricBill.this, OperatorListNew.class);
                i.putExtra("type", "electricity");
                startActivityForResult(i, 100);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    amount = etAmount.getText().toString();
                    number = etBillNumber.getText().toString();
                    biller_name = etBillerName.getText().toString();
                    mobile_number = etMobileNumber.getText().toString();
                    selected_due_date = etDate.getText().toString();
                    amount = etAmount.getText().toString();
                    mShowDialog(getString(R.string.refund_alert));
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    amount = etAmount.getText().toString();
                    number = etBillNumber.getText().toString();
                    biller_name = etBillerName.getText().toString();
                    mobile_number = etMobileNumber.getText().toString();
                    selected_due_date = etDate.getText().toString();
                    amount = etAmount.getText().toString();

                    transaction_type = "validate";
                    String url = Constents.URL.baseUrl + "api/android/billpay/transaction?apptoken=" +
                            SharedPrefs.getValue(ElectricBill.this, SharedPrefs.APP_TOKEN) + "&user_id=" +
                                    SharedPrefs.getValue(ElectricBill.this, SharedPrefs.USER_ID) + "&type="
                                    + transaction_type + "&provider_id=" + provide_id + "&number=" + number + "&mobile=" + mobile_number +
                                    "&amount=" + amount + "&biller=" + biller_name + "&duedate=" + selected_due_date + "&bu=" + bu;

                    REQUEST_TYPE = 1;
                    networkCallUsingVolleyApi(url);
                }
            }
        });


        String fDate = FiscalDate.getFinancialDate(Calendar.getInstance());
        etDate.setText(fDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final AlertDialog.Builder adb = new AlertDialog.Builder(
                        ElectricBill.this);
                final View view = LayoutInflater.from(ElectricBill.this).inflate(R.layout.date_picker, null);
                adb.setView(view);
                final Dialog dialog;
                adb.setPositiveButton(R.string.add_dialog,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                DatePicker etDatePicker = view.findViewById(R.id.datePicker1);
                                //etDatePicker.setMinDate(System.currentTimeMillis() - 1000);
                                Calendar cal = GregorianCalendar.getInstance();
                                cal.set(etDatePicker.getYear(), etDatePicker.getMonth(), etDatePicker.getDayOfMonth());
                                Date date = null;
                                date = cal.getTime();
                                String approxDateString = Constents.SHOWING_DATE_FORMAT.format(date);
                                etDate.setText(approxDateString);
                            }
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

        if (etBillerName.getText() == null || etBillerName.getText().length() == 0) {
            Toast.makeText(this, R.string.enter_biller_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etAmount == null || etAmount.getText().length() == 0) {
            Toast.makeText(this, R.string.input_bill_amount, Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(etAmount.getText().toString()) < 10) {
            Toast.makeText(this, R.string.amout_validation, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 100:
                    provide_id = data.getStringExtra("id");
                    operator_name = data.getStringExtra("name");
                    etOperator.setText(operator_name);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void mShowDialog(final String message) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(ElectricBill.this);
        builder1.setMessage("Recharge of " + operator_name + "\nAmount - \u20B9 " + amount + "\nNote - " + message).setTitle(R.string.recharge_confirmation);
        builder1.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();

                String type = "payment";
                String url = Constents.URL.baseUrl + "api/android/billpay/transaction?apptoken=" + SharedPrefs.getValue(ElectricBill.this,
                        SharedPrefs.APP_TOKEN) + "&user_id=" + SharedPrefs.getValue(ElectricBill.this, SharedPrefs.USER_ID) + "&type="
                        + type + "&provider_id=" + provide_id + "&number=" + number + "&mobile=" + mobile_number + "&amount=" + amount + "&biller=" +
                        biller_name + "&duedate=" + selected_due_date + "&bu=" + bu;

                if (TransactionId != null && TransactionId.length() > 0) {
                    url = url + "&TransactionId=" + TransactionId;
                }

                //Log.e("URL: ",":::  "+ url);

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
            showLoader(getString(R.string.loading_text));
            new VolleyGetNetworkCall(this, this, url).netWorkCall();
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
        closeLoader();

        if (REQUEST_TYPE == 1) {
            mVerifyShowResponse(JSonResponse);
        } else {
            Showdata(JSonResponse);
        }
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
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
            Intent intent = new Intent(ElectricBill.this, TransactionReciept.class);
            intent.putExtra("status", status);
            intent.putExtra("message", message);
            intent.putExtra("transactionid", txnid);
            intent.putExtra("transactionrrn", rrn);
            intent.putExtra("transaction_type", "Electric Bill Pay");
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

                    etAmount.setText(amount);
                    etBillerName.setText(biller_name);
                    etDate.setText(selected_due_date);
                }

                Toast.makeText(this, "R.string.bill_fetched", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
