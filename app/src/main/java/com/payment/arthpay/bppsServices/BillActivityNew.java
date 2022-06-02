package com.payment.arthpay.bppsServices;

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

import com.payment.arthpay.R;
import com.payment.arthpay.activity.TransactionReciept;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.bppsServices.model.ParameItem;
import com.payment.arthpay.httpRequest.DemoVolleyNetworkCall;
import com.payment.arthpay.httpRequest.UpdateBillService;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class BillActivityNew extends AppCompatActivity implements DemoVolleyNetworkCall.RequestResponseLis {
    TextView tvLabel, tvTitle;
    EditText etCustomerName, etDueDate, etDueAmount, etPin;
    Button btn;
    ImageView btnBack;
    String amountVal, billerNameVal, dueDateVal, transationIdVal, provider_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_bill_new);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        init();

        btn.setOnClickListener(v -> {
            if (isValid()) {
                mShowDialog(getString(R.string.refund_alert));
            }
        });

        etDueDate.setOnClickListener(arg0 -> {
            final AlertDialog.Builder adb = new AlertDialog.Builder(
                    BillActivityNew.this);
            final View view = LayoutInflater.from(BillActivityNew.this).inflate(R.layout.date_picker, null);
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
        });

        TextView tvWallet = findViewById(R.id.tvBalance);
        TextView tvAeps = findViewById(R.id.tvAeps);
        ImageView imgSync = findViewById(R.id.imgSync);
        String balance = getString(R.string.rupee) + " " + SharedPrefs.getValue(this, SharedPrefs.MAIN_WALLET);
        String aeps = getString(R.string.rupee) + " " + SharedPrefs.getValue(this, SharedPrefs.APES_BALANCE);
        tvWallet.setText(balance);
        tvAeps.setText(aeps);
        imgSync.setOnClickListener(v -> new UpdateBillService(this, tvWallet, tvAeps));

        if (Constents.IS_TPIN_ACTIVE)
            findViewById(R.id.layoutPin).setVisibility(View.VISIBLE);
    }

    private void init() {
        tvLabel = findViewById(R.id.tvLabel);
        etCustomerName = findViewById(R.id.etCustomerName);
        etDueDate = findViewById(R.id.etDueDate);
        etDueAmount = findViewById(R.id.etDueAmount);
        tvTitle = findViewById(R.id.tvTitle);
        btn = findViewById(R.id.btn);
        etPin = findViewById(R.id.etPin);
        setInitial();
    }

    private void setInitial() {
        amountVal = getIntent().getStringExtra("amount");
        billerNameVal = getIntent().getStringExtra("billerName");
        dueDateVal = getIntent().getStringExtra("dueDate");
        transationIdVal = getIntent().getStringExtra("transactionId");
        provider_id = getIntent().getStringExtra("provider_id");

        if (amountVal != null && amountVal.length() > 0) {
            etDueAmount.setText(amountVal);
        }

        if (billerNameVal != null && billerNameVal.length() > 0) {
            etCustomerName.setText(billerNameVal);
        }

        if (dueDateVal != null && dueDateVal.length() > 0) {
            etDueDate.setText(dueDateVal);
        }
        tvTitle.setText(Constents.BILL_MODEL.getType().toUpperCase());

        StringBuilder value = new StringBuilder();
        value.append("Operator Name").append(" : ").append(Constents.BILL_MODEL.getName());
        for (ParameItem item : Constents.BILL_MODEL.getParame()) {
            value.append("\n").append(item.getParamname()).append(" : ").append(item.getFieldInputValue());
        }
        tvLabel.setText(value);
    }

    private boolean isValid() {
        if (etCustomerName.getText() == null || etCustomerName.getText().length() == 0) {
            Toast.makeText(this, R.string.enter_biller_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        String amount = etDueAmount.getText().toString();
        if (etDueAmount.getText() == null || etDueAmount.getText().length() == 0) {
            Toast.makeText(this, R.string.enter_bill_amount, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Constents.IS_TPIN_ACTIVE && etPin.getText().length() == 0) {
            Toast.makeText(this, "Pin is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("apptoken", SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN));
        map.put("user_id", SharedPrefs.getValue(this, SharedPrefs.USER_ID));
        map.put("type", "payment");
        map.put("provider_id", provider_id);
        map.put("biller", etCustomerName.getText().toString());
        map.put("duedate", etDueDate.getText().toString());
        map.put("amount", etDueAmount.getText().toString());
        map.put("bu", "");
        if (etPin.getText().toString().length() > 0)
            map.put("pin", etPin.getText().toString());
        if (transationIdVal != null && transationIdVal.length() > 0)
            map.put("TransactionId", transationIdVal);

        String approxDateString = Constents.SHOWING_DATE_FORMAT.format(new Date());
        map.put("dateTime", approxDateString);

        if (Constents.BILL_MODEL != null) {
            for (int i = 0; i < Constents.BILL_MODEL.getParame().size(); i++) {
                String key = "number" + i;
                map.put(key, Constents.BILL_MODEL.getParame().get(i).getFieldInputValue());
            }
        }

        //Log.e("param : ", "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        Print.P("param:  " + new JSONObject(map).toString());
        return map;
    }


    protected void mShowDialog(final String message) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(BillActivityNew.this);
        builder1.setMessage("Recharge of " + Constents.BILL_MODEL.getName() + "\nAmount - \u20B9 " + etDueAmount.getText().toString() + "\nNote - " + message).setTitle(R.string.recharge_confirmation);
        builder1.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();

                String url = Constents.URL.baseUrl + "api/android/billpay/transaction";
                Print.P("url: " + url);
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
            new DemoVolleyNetworkCall(this, this, url).netWorkCall(param());
        } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
        }
    }

    private AlertDialog loaderDialog;

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P("res " + JSonResponse);
        //closeLoader();
        Showdata(JSonResponse);
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
            Intent intent = new Intent(BillActivityNew.this, TransactionReciept.class);
            intent.putExtra("status", status);
            intent.putExtra("message", message);
            intent.putExtra("transactionid", txnid);
            intent.putExtra("transactionrrn", rrn);
            intent.putExtra("transaction_type", "Bill Pay");
            intent.putExtra("operator", Constents.BILL_MODEL.getName());
            intent.putExtra("number", etDueDate.getText().toString());
            intent.putExtra("price", etDueAmount.getText().toString());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

}
