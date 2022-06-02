package com.payment.arthpay.moneyTransfer.mDMT;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.model.BeneficiaryItems;
import com.payment.arthpay.utill.AppManager;
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

public class MTransaction extends AppCompatActivity {
    TextView sendernumber, benenametext, accountnumber, bankname, sendername;

    EditText editamount, etPin;
    Button sendbutton;
    ProgressDialog dialog;
    String beneid, mobile, amount, txntype, name, benename, beneaccount, benebank, beneifsc, myJSON, benemobile;
    BeneficiaryItems beneficiary_items;

    RadioGroup radioGroup;
    RadioButton radiobutton_imps, radiobutton_neft, TxnType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sendernumber = findViewById(R.id.sendernumber);
        etPin = findViewById(R.id.etPin);
        sendername = findViewById(R.id.sendername);
        benenametext = findViewById(R.id.benenametext);
        accountnumber = findViewById(R.id.accountnumber);
        bankname = findViewById(R.id.bankname);
        editamount = findViewById(R.id.editamount);
        radioGroup = findViewById(R.id.radioGroup);
        radiobutton_imps = findViewById(R.id.radiobutton_imps);
        radiobutton_neft = findViewById(R.id.radiobutton_neft);

        radiobutton_imps.setChecked(true);
        beneficiary_items = (BeneficiaryItems) getIntent().getExtras().getSerializable("DATA");
        benenametext.setText(beneficiary_items.getBenename());
        bankname.setText(beneficiary_items.getBenebank() + " (" + beneficiary_items.getBeneifsc() + ")");
        accountnumber.setText(beneficiary_items.getBeneaccount());
        sendernumber.setText(beneficiary_items.getSendernumber());
        sendername.setText(beneficiary_items.getSendername());

        sendbutton = findViewById(R.id.sendbutton);
        sendbutton.setOnClickListener(v -> {
            if (AppManager.isOnline(MTransaction.this)) {
                if (editamount.getText().toString().equals("")) {
                    Toast.makeText(MTransaction.this, "Enter amount", Toast.LENGTH_SHORT).show();
                } else if (Constents.IS_TPIN_ACTIVE && etPin.getText().toString().equals("")) {
                    Toast.makeText(MTransaction.this, "Pin required", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(editamount.getText().toString()) < 100 || Integer.parseInt(editamount.getText().toString()) > 25000) {
                    Toast.makeText(MTransaction.this, "Amount should be between 100 to 25000", Toast.LENGTH_SHORT).show();
                } else {
                    txntype = "";
                    beneifsc = beneficiary_items.getBeneifsc();
                    mobile = beneficiary_items.getSendernumber();
                    beneid = beneficiary_items.getBeneid();
                    beneaccount = beneficiary_items.getBeneaccount();
                    amount = editamount.getText().toString();
                    name = beneficiary_items.getSendername();
                    benebank = beneficiary_items.getBenebankid();
                    benemobile = beneficiary_items.getBenemobile();
                    benename = beneficiary_items.getBenename().replaceAll(" ", "%20");
                    mShowDialog("Please confirm Bank, Account Number and amount, transfer will not reverse at any circumstances.");
                }
            } else {
                Toast.makeText(MTransaction.this, "No Connection", Toast.LENGTH_SHORT).show();
            }
        });

        if (Constents.IS_TPIN_ACTIVE)
            findViewById(R.id.secTpin).setVisibility(View.VISIBLE);
    }

    protected void SendMoney() {
        class Send extends AsyncTask<String, String, String> {
            HttpURLConnection httpURLConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(MTransaction.this);
                dialog.setMessage("Please Wait and don't press the back or Refresh...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                myJSON = s;
                //Log.e("transaction response", s);
                ShowTransferMessage();
                dialog.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {
                StringBuilder result = new StringBuilder();
                try {
                    URL url = new URL(Constents.URL.baseUrl + "api/android/dmt/transaction?apptoken="
                            + SharedPrefs.getValue(MTransaction.this, SharedPrefs.APP_TOKEN) + "&user_id="
                            + SharedPrefs.getValue(MTransaction.this, SharedPrefs.USER_ID) + "&type=transfer&mobile=" + mobile +
                            "&benebank=" + benebank + "&beneifsc=" + beneifsc + "&benemobile=" + benemobile + "&beneaccount=" + beneaccount +
                            "&txntype=" + txntype + "&benename=" + benename +
                            "&name=" + name +
                            "&pin=" + etPin.getText().toString() +
                            "&amount=" + amount + "&beneid="
                            + beneid + "&device_id=" + Constents.MOBILE_ID);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    httpURLConnection.disconnect();
                }

                return result.toString();
            }
        }

        Send send = new Send();
        send.execute();
    }

    protected void ShowTransferMessage() {
        String status = "";
        String message = "";
        String ref = "";
        try {
            JSONObject jsonObject = new JSONObject(myJSON);
            //Log.e("Log", myJSON);
            if (jsonObject.has("statuscode")) {
                status = jsonObject.getString("statuscode");
            }

            if (jsonObject.has("message")) {
                message = jsonObject.getString("message");
            }

            if (jsonObject.has("data")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if (jsonObject1.has("rrn")) {
                        ref = jsonObject1.getString("rrn");
                    } else {
                        ref = jsonObject1.getString("message");
                    }


                    message = message + "\nRs " + jsonObject1.getString("amount") + " , " +
                            "Ref - " + ref + " (" + jsonObject1.getString("message") + ")";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!status.equals("")) {
            //String status = jsonObject.getString("statuscode");
            if (status.equalsIgnoreCase("UA")) {
                AppManager.getInstance().logoutApp(MTransaction.this);
            } else if (status.equalsIgnoreCase("txn")) {
                mShowResponse(status, message);
            } else if (status.equalsIgnoreCase("failure") || status.equalsIgnoreCase("fail")) {
                mShowResponse(status, message);
            } else {
                mShowResponse(status, message);
            }
        } else {
            mShowResponse("a", "Something went wrong...");
        }
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

    protected void mShowDialog(final String message) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(MTransaction.this);
        builder1.setMessage("Account -  " + beneaccount + "\nAmount - \u20B9 " + amount + "\nNote - " + message).setTitle("Transfer Confirmation");

        builder1.setCancelable(false)
                .setPositiveButton("Confirm", (dialog, i) -> {
                    if (AppManager.isOnline(MTransaction.this)) {
                        SendMoney();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder1.create();
        alert.show();
    }

    protected void mShowResponse(final String status, final String message) {
        final AlertDialog alertDialog;
        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2 = inflater2.inflate(R.layout.custome_layout_fund_transfer, null);
        RelativeLayout rl_title = v2.findViewById(R.id.rl_title);
        Button button_ok = v2.findViewById(R.id.button_ok);
        ImageView imageview_status_image = v2.findViewById(R.id.imageview_status_image);
        TextView textview_message = v2.findViewById(R.id.textview_message);

        if (status.equalsIgnoreCase("err")) {
            imageview_status_image.setImageDrawable(getResources().getDrawable(R.drawable.cancel_icon));
            textview_message.setText("Rs " + amount + "\n" + message);
            textview_message.setTextColor(getResources().getColor(R.color.orange));
        } else if (status.equalsIgnoreCase("txn")) {
            rl_title.setBackgroundColor(getResources().getColor(R.color.green));
            button_ok.setBackgroundColor(getResources().getColor(R.color.green));
            imageview_status_image.setImageDrawable(getResources().getDrawable(R.drawable.success_icon));
            textview_message.setText("Rs " + amount + "\n" + message);
            textview_message.setTextColor(getResources().getColor(R.color.green));
        } else {
            imageview_status_image.setImageDrawable(getResources().getDrawable(R.drawable.cancel_icon));
            textview_message.setText("Rs " + amount + "\n" + message);
            textview_message.setTextColor(getResources().getColor(R.color.orange));
        }

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(MTransaction.this);
        builder2.setCancelable(false);
        builder2.setView(v2);
        alertDialog = builder2.create();
        button_ok.setOnClickListener(view -> {
            alertDialog.dismiss();
            if (status.equalsIgnoreCase("txn")) {
                finish();
            }
        });
        alertDialog.show();
    }
}
