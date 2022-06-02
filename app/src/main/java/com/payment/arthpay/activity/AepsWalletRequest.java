package com.payment.arthpay.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AepsWalletRequest extends AppCompatActivity {
    RelativeLayout rl_type;
    TextView textview_type;
    LinearLayout ll_bank_detail;
    EditText edittext_bank_name, edittext_account_number, edittext_ifsc, edittext_amount, etPin;
    Button button_submit;
    String selected_payment_type = "";
    ProgressDialog dialog;
    String mode = "NEFT";

    private void addImpaAepsOption() {
        RadioButton radioNeft = findViewById(R.id.radioNeft);
        RadioButton radioImps = findViewById(R.id.radioImps);
        radioImps.setOnClickListener(view -> mode = "IMPS");
        radioNeft.setOnClickListener(view -> mode = "NEFT");
    }

    private void manageAccount(String bankNme, String ifsc, String Acc) {
        if (Acc != null && Acc.length() > 0 && !Acc.equalsIgnoreCase("null")) {
            edittext_account_number.setEnabled(false);
            edittext_account_number.setText(Acc);
        }

        if (bankNme != null && bankNme.length() > 0 && !bankNme.equalsIgnoreCase("null")) {
            edittext_bank_name.setText(bankNme);
            edittext_bank_name.setEnabled(false);
        }

        if (ifsc != null && ifsc.length() > 0 && !ifsc.equalsIgnoreCase("null")) {
            edittext_ifsc.setText(ifsc);
            edittext_ifsc.setEnabled(false);
        }

       /* String slug = SharedPrefs.getValue(this, SharedPrefs.ROLE_SLUG);
        if (slug != null && slug.equalsIgnoreCase("retailer")) {
            edittext_account_number.setEnabled(false);
            edittext_bank_name.setEnabled(false);
            edittext_ifsc.setEnabled(false);
        }*/
    }

    String accountNumber, bankName, Ifsc;

    private void accountCondition() {
        RadioButton accOne, accTwo, accThree;
        accOne = findViewById(R.id.rbAccount1);
        accTwo = findViewById(R.id.rbAccount2);
        accThree = findViewById(R.id.rbAccount3);

        accountNumber = SharedPrefs.getValue(this, SharedPrefs.ACCOUNT);
        bankName = SharedPrefs.getValue(this, SharedPrefs.BANK);
        Ifsc = SharedPrefs.getValue(this, SharedPrefs.IFSC);
        manageAccount(bankName, Ifsc, accountNumber);

        accOne.setOnClickListener(v -> {
            accountNumber = SharedPrefs.getValue(this, SharedPrefs.ACCOUNT);
            bankName = SharedPrefs.getValue(this, SharedPrefs.BANK);
            Ifsc = SharedPrefs.getValue(this, SharedPrefs.IFSC);
            manageAccount(bankName, Ifsc, accountNumber);
        });

        accTwo.setOnClickListener(v -> {
            accountNumber = SharedPrefs.getValue(this, SharedPrefs.ACCOUNT2);
            bankName = SharedPrefs.getValue(this, SharedPrefs.BANK2);
            Ifsc = SharedPrefs.getValue(this, SharedPrefs.IFSC2);
            manageAccount(bankName, Ifsc, accountNumber);
        });

        accThree.setOnClickListener(v -> {
            accountNumber = SharedPrefs.getValue(this, SharedPrefs.ACCOUNT3);
            bankName = SharedPrefs.getValue(this, SharedPrefs.BANK3);
            Ifsc = SharedPrefs.getValue(this, SharedPrefs.IFSC3);
            manageAccount(bankName, Ifsc, accountNumber);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps_wallet_request);
        addImpaAepsOption();
        edittext_bank_name = findViewById(R.id.edittext_bank_name);
        edittext_account_number = findViewById(R.id.edittext_account_number);
        edittext_ifsc = findViewById(R.id.edittext_ifsc);
        accountCondition();
        rl_type = findViewById(R.id.rl_type);
        textview_type = findViewById(R.id.textview_type);
        rl_type.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(AepsWalletRequest.this, rl_type);
            popupMenu.getMenu().add("To Wallet");
            popupMenu.getMenu().add("To Bank");
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                String text = item.getTitle().toString();
                selected_payment_type = text.substring(3).toLowerCase();
                System.out.print("AEPS type:       " + selected_payment_type);
                textview_type.setText(text);
                return true;
            });
        });

        ll_bank_detail = findViewById(R.id.ll_bank_detail);

        etPin = findViewById(R.id.etPin);
        edittext_amount = findViewById(R.id.edittext_amount);
        button_submit = findViewById(R.id.button_submit);

        button_submit.setOnClickListener(v -> {
            if (AppManager.isOnline(AepsWalletRequest.this)) {
                if (selected_payment_type.equalsIgnoreCase("")) {
                    Toast.makeText(AepsWalletRequest.this, "Please select payment type", Toast.LENGTH_SHORT).show();
                } else if (Constents.IS_TPIN_ACTIVE && etPin.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(AepsWalletRequest.this, "Pin is required", Toast.LENGTH_SHORT).show();
                } else if (selected_payment_type.equalsIgnoreCase("wallet") && edittext_amount.getText().toString().equals("")) {
                    Toast.makeText(AepsWalletRequest.this, "PLease enter request amount", Toast.LENGTH_SHORT).show();
                } else if (selected_payment_type.equalsIgnoreCase("bank") && edittext_bank_name.getText().toString().equals("")) {
                    Toast.makeText(AepsWalletRequest.this, "Please enter bank name were amount deposited", Toast.LENGTH_SHORT).show();
                } else if (selected_payment_type.equalsIgnoreCase("bank") && edittext_account_number.getText().toString().equals("")) {
                    Toast.makeText(AepsWalletRequest.this, "Please enter account number in which amount deposited", Toast.LENGTH_SHORT).show();
                } else if (selected_payment_type.equalsIgnoreCase("bank") && edittext_ifsc.getText().toString().equals("")) {
                    Toast.makeText(AepsWalletRequest.this, "Please enter bank IFSC", Toast.LENGTH_SHORT).show();
                } else if (selected_payment_type.equalsIgnoreCase("bank") && edittext_amount.getText().toString().equals("")) {
                    Toast.makeText(AepsWalletRequest.this, "Please enter request amount", Toast.LENGTH_SHORT).show();
                } else {
                    String bank_name = edittext_bank_name.getText().toString().replaceAll(" ", "%20");
                    String account = edittext_account_number.getText().toString();
                    String ifsc = edittext_ifsc.getText().toString();
                    String amount = edittext_amount.getText().toString();
                    String pin = etPin.getText().toString();
                    mSubmitRequest(SharedPrefs.getValue(AepsWalletRequest.this, SharedPrefs.APP_TOKEN),
                            SharedPrefs.getValue(AepsWalletRequest.this, SharedPrefs.USER_ID),
                            selected_payment_type, amount, account, bank_name, ifsc, pin);
                }
            } else {
                Toast.makeText(AepsWalletRequest.this, "NO internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        if (Constents.IS_TPIN_ACTIVE)
            findViewById(R.id.secPin).setVisibility(View.VISIBLE);
    }

    private void mSubmitRequest(final String apptoken, final String user_id, final String payment_type,
                                final String amount, final String account_number, final String bank,
                                final String ifsc, final String pin) {
        class getJSONData extends AsyncTask<String, String, String> {
            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(AepsWalletRequest.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);

            }

            @Override
            protected String doInBackground(String... args) {

                BufferedReader reader;
                StringBuffer buffer;
                String res = null;

                try {
                    String u = Constents.URL.baseUrl + "api/android/fundrequest?apptoken=" + apptoken + "&user_id=" + user_id +
                            "&type=" + payment_type + "&amount=" + amount + "&account=" + account_number + "&bank=" + bank +
                            "&ifsc=" + ifsc
                            + "&mode=" + mode
                            + "&pin=" + pin
                            + "&device_id=" + Constents.MOBILE_ID;
                    URL url = new URL(u);
                    Print.P(u);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(40000);
                    con.setConnectTimeout(40000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept", "application/json");
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    con.setDoInput(true);
                    con.setDoOutput(true);

                    int status = con.getResponseCode();
                    InputStream inputStream;
                    if (status == HttpURLConnection.HTTP_OK) {
                        inputStream = con.getInputStream();
                    } else {
                        inputStream = con.getErrorStream();
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    buffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    res = buffer.toString();
                } catch (Exception e) {
                    AppManager.appendLog("getting error in aeps Req");
                    AppManager.appendLog(e);
                }
                return res;
            }

            @Override
            protected void onPostExecute(String result) {
                dialog.dismiss();
                //Log.e("submit response",result);

                if (result != null && !result.equals("")) {
                    String statuscode = "";
                    String message = "";
                    String txnid = "";
                    try {
                        JSONObject jsonObject = new JSONObject(result);

                        if (jsonObject.has("statuscode")) {
                            statuscode = jsonObject.getString("statuscode");
                        } else {
                            statuscode = "ERR";
                        }

                        if (jsonObject.has("message")) {
                            message = jsonObject.getString("message");
                        } else {
                            message = "Something went wrong";
                        }

                        if (!statuscode.equals("")) {
                            if (statuscode.equalsIgnoreCase("UA")) {
                                AppManager.getInstance().logoutApp(AepsWalletRequest.this);
                            } else if (statuscode.equalsIgnoreCase("txn")) {
                                txnid = jsonObject.getString("txnid");
                                Intent intent = new Intent(AepsWalletRequest.this, TransactionReciept.class);
                                intent.putExtra("status", statuscode);
                                intent.putExtra("message", message);
                                intent.putExtra("transactionid", txnid);
                                intent.putExtra("transactionrrn", txnid);
                                intent.putExtra("transaction_type", "payout");
                                intent.putExtra("operator", payment_type);
                                intent.putExtra("number", account_number);
                                intent.putExtra("price", amount);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(AepsWalletRequest.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AepsWalletRequest.this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AepsWalletRequest.this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                }
            }
        }

        getJSONData getJSONData = new getJSONData();
        getJSONData.execute();
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
}