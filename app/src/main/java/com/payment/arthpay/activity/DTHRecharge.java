package com.payment.arthpay.activity;

import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
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

import com.payment.arthpay.R;
import com.payment.arthpay.RechargePlans.ViewPlans;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.httpRequest.UpdateBillService;
import com.payment.arthpay.httpRequest.VolleyGetNetworkCall;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


public class DTHRecharge extends AppCompatActivity implements
        VolleyGetNetworkCall.RequestResponseLis {

    private Button btn;
    private ImageView imgKyc;
    private EditText etOperator, etAmount, etCardNumber, etPin;
    private ImageView imgContact;
    private String operator_name;
    private String number, amount, provide_id;
    private TextView tvInfo, tvOffer, tvRefresh;
    private ImageView imgCustomerInfo ;
    private int REQUEST_TYPE = 0;

    private void init() {
        etCardNumber = findViewById(R.id.etCardNumber);
        tvRefresh = findViewById(R.id.tvRefresh);
        tvInfo = findViewById(R.id.tvInfo);
        etAmount = findViewById(R.id.etAmount);
        etOperator = findViewById(R.id.etOperator);
        imgCustomerInfo = findViewById(R.id.imgCustomerInfo);
        btn = findViewById(R.id.btn);
        tvOffer = findViewById(R.id.tvOffer);
        etPin = findViewById(R.id.etPin);

        if (Constents.IS_TPIN_ACTIVE)
            findViewById(R.id.etPin).setVisibility(View.VISIBLE);

        tvRefresh.setOnClickListener(v -> {
            if (isValidPlan()) {
                REQUEST_TYPE = 3;
                String url = Constents.URL.baseUrl + "api/android/recharge/dthhavyrefresh?apptoken=" +
                        SharedPrefs.getValue(DTHRecharge.this, SharedPrefs.APP_TOKEN) + "&user_id=" +
                        SharedPrefs.getValue(DTHRecharge.this, SharedPrefs.USER_ID) +
                        "&operator=" + provide_id +
                        "&number=" + etCardNumber.getText().toString() +
                        "&device_id=" + Constents.MOBILE_ID;
                Print.P("URL : "+ url);
                networkCallUsingVolleyApi(url);
            }
        });
    }

    private void btnClear() {
        etCardNumber.setText("");
        tvInfo.setText("");
        etAmount.setText("");
        etOperator.setText("");
        etPin.setText("");
        number = "";
        amount = "";
        provide_id = "";
    }

    private void browsePlans() {
        TextView tvPlan = findViewById(R.id.tvPlans);
        tvPlan.setOnClickListener(v -> {
            if (isValidPlan()) {
                Intent i = new Intent(DTHRecharge.this, ViewPlans.class);
                i.putExtra("operator", provide_id);
                i.putExtra("operatorName", operator_name);
                i.putExtra("contact", etCardNumber.getText().toString());
                i.putExtra("type", "mobile");
                startActivityForResult(i, 121);
            }
        });

        tvOffer.setOnClickListener(v -> {
            if (isValidPlan()) {
                Intent i = new Intent(DTHRecharge.this, ViewPlans.class);
                i.putExtra("operator", provide_id);
                i.putExtra("operatorName", operator_name);
                i.putExtra("contact", etCardNumber.getText().toString());
                i.putExtra("type", "mobile");
                i.putExtra("type1", "offer");
                startActivityForResult(i, 121);
            }
        });
    }

    boolean isValidPlan() {
        if (etOperator.getText().toString().length() == 0) {
            Toast.makeText(this, "Please Select Operator", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etCardNumber.getText().toString().length() == 0) {
            Toast.makeText(this, "Please Select Card Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dth_recharge);

        init();

        browsePlans();

        etOperator.setOnClickListener(v -> {
            Intent i = new Intent(DTHRecharge.this, OperatorList.class);
            i.putExtra("type", "dth");
            startActivityForResult(i, 100);
        });

        btn.setOnClickListener(v -> {
            if (isValid()) {
                number = etCardNumber.getText().toString();
                amount = etAmount.getText().toString();
                mShowDialog("Please confirm operator, number and amount, wrong recharge will not reverse at any circumstances.");
            }
        });

        imgCustomerInfo.setOnClickListener(v -> {
            if (isValidPlan()) {
                REQUEST_TYPE = 1;
                String url = Constents.URL.baseUrl + "api/android/recharge/dthinfo?apptoken=" +
                        SharedPrefs.getValue(DTHRecharge.this, SharedPrefs.APP_TOKEN) + "&user_id=" +
                        SharedPrefs.getValue(DTHRecharge.this, SharedPrefs.USER_ID) +
                        "&operator=" + provide_id +
                        "&number=" + etCardNumber.getText().toString() +
                        "&device_id=" + Constents.MOBILE_ID;
                networkCallUsingVolleyApi(url);
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
        if (etCardNumber.getText().length() == 0) {
            Toast.makeText(this, "Please input card number.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etOperator.getText().length() == 0) {
            Toast.makeText(this, "Please select operator", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etAmount == null || etAmount.getText().length() == 0) {
            Toast.makeText(this, "Please input recharge plan amount", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(etAmount.getText().toString()) < 10) {
            Toast.makeText(this, "Please input recharge plan amount greater than 10", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Constents.IS_TPIN_ACTIVE && etPin.getText().length() == 0) {
            Toast.makeText(this, "Pin is required field", Toast.LENGTH_SHORT).show();
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
                case 121:
                    String amount = data.getStringExtra("amount");
                    etAmount.setText(amount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void mShowDialog(final String message) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(DTHRecharge.this);
        builder1.setMessage("Recharge of " + operator_name + "\nAmount - \u20B9 " + amount + "\nNote - " + message).setTitle("Recharge Confirmation");
        builder1.setPositiveButton("Confirm", (dialog, i) -> {
            dialog.dismiss();
            String url = Constents.URL.baseUrl + "api/android/recharge/pay?apptoken=" +
                    SharedPrefs.getValue(DTHRecharge.this, SharedPrefs.APP_TOKEN) + "&user_id=" +
                    SharedPrefs.getValue(DTHRecharge.this, SharedPrefs.USER_ID) +
                    "&provider_id=" + provide_id +
                    "&pin=" + etPin.getText().toString() +
                    "&amount=" + amount + "&number=" + number + "&device_id=" + Constents.MOBILE_ID;
            REQUEST_TYPE = 0;
            networkCallUsingVolleyApi(url);
        }).setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder1.create();
        alert.setCancelable(false);
        alert.show();
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

    @Override
    public void onSuccessRequest(String JSonResponse) {
        closeLoader();
        try {
            if (REQUEST_TYPE == 0) {
                Showdata(JSonResponse);
            } else {
                JSONObject obj = new JSONObject(JSonResponse);
                String status = obj.getString("statuscode");
                if (status.equalsIgnoreCase("TXN")) {
                    JSONObject dataObj = obj.getJSONObject("data");
                    Iterator<String> rsKeys = dataObj.keys();
                    StringBuilder details = new StringBuilder();
                    while (rsKeys.hasNext()) {
                        String key = rsKeys.next();
                        String value = dataObj.getString(key);
                        details.append("\n").append(key.toUpperCase()).append(" : ").append(value);
                    }
                    tvInfo.setVisibility(View.VISIBLE);
                    tvInfo.setText(details.toString());
                } else {
                    tvInfo.setText("");
                    tvInfo.setVisibility(View.GONE);
                    Toast.makeText(this, "Info Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
        if (REQUEST_TYPE == 0) {
            Toast.makeText(this, "Recharge Failed", Toast.LENGTH_SHORT).show();
        } else {
            tvInfo.setText("");
            tvInfo.setVisibility(View.GONE);
            Toast.makeText(this, "Info Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void Showdata(final String json) {
        Print.P(json);
        String status = "";
        String message = "";
        String txnid = "";
        String rrn = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("status")) {
                status = jsonObject.getString("status");
            } else {
                status = jsonObject.getString("statuscode");
            }
            message = jsonObject.getString("message");
            txnid = jsonObject.getString("txnid");
            rrn = jsonObject.getString("rrn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //String status = jsonObject.getString("statuscode");
        if (status.equalsIgnoreCase("UA")) {
            AppManager.getInstance().logoutApp(DTHRecharge.this);
        } else if (status.equalsIgnoreCase("TXN")) {
            Intent intent = new Intent(DTHRecharge.this, TransactionReciept.class);
            intent.putExtra("status", status);
            intent.putExtra("message", message);
            intent.putExtra("transactionid", txnid);
            intent.putExtra("transactionrrn", rrn);
            intent.putExtra("transaction_type", "DTH Recharge Request");
            intent.putExtra("operator", operator_name);
            intent.putExtra("number", number);
            intent.putExtra("price", amount);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

}
