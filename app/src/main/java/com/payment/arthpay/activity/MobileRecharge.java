package com.payment.arthpay.activity;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;


public class MobileRecharge extends AppCompatActivity implements VolleyGetNetworkCall.RequestResponseLis {

    private Button btn;
    private ImageView imgKyc;
    private EditText etOperator, etAmount, etContact, etPin;
    private ImageView imgContact;
    private String operator_name;
    private String number, amount, provide_id;
    private void init() {
        etOperator = findViewById(R.id.etOperator);
        etAmount = findViewById(R.id.etAmount);
        etContact = findViewById(R.id.etContact);
        etPin = findViewById(R.id.etPin);
        //imgContact = findViewById(R.id.imgContact);
        btn = findViewById(R.id.btn);

        if (Constents.IS_TPIN_ACTIVE)
            findViewById(R.id.secTpin).setVisibility(View.VISIBLE);
    }

    private void browsePlans() {
        TextView tvPlan = findViewById(R.id.tvPlans);
        TextView tvOffer = findViewById(R.id.tvOffer);
        tvPlan.setOnClickListener(v -> {
            if (isValidPlan()) {
                Intent i = new Intent(MobileRecharge.this, ViewPlans.class);
                i.putExtra("operator", provide_id);
                i.putExtra("operatorName", operator_name);
                i.putExtra("contact", etContact.getText().toString());
                i.putExtra("type", "mobile");
                startActivityForResult(i, 121);
            }
        });

        tvOffer.setOnClickListener(v -> {
            if (isValidPlan()) {
                Intent i = new Intent(MobileRecharge.this, ViewPlans.class);
                i.putExtra("operator", provide_id);
                i.putExtra("operatorName", operator_name);
                i.putExtra("contact", etContact.getText().toString());
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

        if (etContact.getText().toString().length() == 0) {
            Toast.makeText(this, "Please Select Contact", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_recharge);

        init();
        browsePlans();
        etContact.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etContact.getRight() - etContact.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(intent, 10);
                        return true;
                    }
                }
                return false;
            }
        });

        etOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MobileRecharge.this, OperatorList.class);
                i.putExtra("type", "mobile");
                startActivityForResult(i, 100);
            }
        });

        btn.setOnClickListener(v -> {
            if (isValid()) {
                number = etContact.getText().toString();
                amount = etAmount.getText().toString();
                mShowDialog("Please confirm operator, number and amount, wrong recharge will not reverse at any circumstances.");
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
        if (etContact.getText().length() == 0) {
            Toast.makeText(this, "Please input or select contact number.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etOperator.getText().length() == 0) {
            Toast.makeText(this, "Please select operator", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etAmount.getText().length() == 0) {
            Toast.makeText(this, "Please input recharge plan amount", Toast.LENGTH_SHORT).show();
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
                case (10):
                    if (resultCode == Activity.RESULT_OK) {
                        Cursor cursor = null;
                        String phoneNo = null;
                        String name = null;
                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        phoneNo = cursor.getString(phoneIndex);
                        phoneNo = phoneNo.replaceAll(" ", "");
                        if (phoneNo.startsWith("+91")) {
                            etContact.setText(phoneNo.substring(3, 13));
                        } else {
                            etContact.setText(phoneNo);
                        }
                    }
                    break;
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
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(MobileRecharge.this);
        builder1.setMessage("Recharge of " + operator_name + "\nAmount - \u20B9 " + amount + "\nNote - " + message).setTitle("Recharge Confirmation");
        builder1.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                String url = Constents.URL.baseUrl + "api/android/recharge/pay?apptoken=" +
                        SharedPrefs.getValue(MobileRecharge.this, SharedPrefs.APP_TOKEN) + "&user_id=" +
                        SharedPrefs.getValue(MobileRecharge.this, SharedPrefs.USER_ID) + "&provider_id=" + provide_id +
                        "&amount=" + amount +
                        "&number=" + number +
                        "&pin=" + etPin.getText().toString() +
                        "&device_id=" + Constents.MOBILE_ID;

                networkCallUsingVolleyApi(url);
            }
        })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
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

    private android.app.AlertDialog loaderDialog;

    private void showLoader(String loaderMsg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
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
        Showdata(JSonResponse);
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
        Toast.makeText(this, "Recharge Failed", Toast.LENGTH_SHORT).show();
    }

    private void Showdata(final String json) {
        String status = "";
        String message = "";
        String txnid = "";
        String rrn = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            status = jsonObject.getString("statuscode");
            message = jsonObject.getString("message");
            txnid = jsonObject.getString("txnid");
            rrn = jsonObject.getString("rrn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (status.equalsIgnoreCase("TXN")) {
            Intent intent = new Intent(MobileRecharge.this, TransactionReciept.class);
            intent.putExtra("status", status);
            intent.putExtra("message", message);
            intent.putExtra("transactionid", txnid);
            intent.putExtra("transactionrrn", rrn);
            intent.putExtra("transaction_type", "Mobile Recharge Request");
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
