package com.payment.arthpay.moneyTransfer.mDMT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.payment.arthpay.R;
import com.payment.arthpay.activity.PinReset;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.httpRequest.VolleyPostNetworkCall;
import com.payment.arthpay.model.BeneficiaryItems;
import com.payment.arthpay.moneyTransfer.model.LocalInvoiceModel;
import com.payment.arthpay.reports.MyUtil;
import com.payment.arthpay.reports.invoice.ReportInvoice;
import com.payment.arthpay.reports.invoice.model.InvoiceModel;
import com.payment.arthpay.utill.AppHandler;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DMTTransaction extends AppCompatActivity implements
        VolleyPostNetworkCall.RequestResponseLis {
    Dialog dialog;
    String rootStatus = "";
    private String SENDER_NAME, SENDER_NUMBER;
    private TextView tvDetails, tvGenPin;
    private EditText etAmount, etTPin;
    private BeneficiaryItems model;
    private Button btnProceed;
    private ArrayList<LocalInvoiceModel> dataList;
    private String urlString;

    private void init() {
        SENDER_NAME = getIntent().getStringExtra("sender_name");
        urlString = getIntent().getStringExtra("url");
        SENDER_NUMBER = getIntent().getStringExtra("sender_number");
        tvDetails = findViewById(R.id.tvDetail);
        etTPin = findViewById(R.id.etTPin);
        tvGenPin = findViewById(R.id.tvGenPin);
        String details = "Sender Name : " + SENDER_NAME;
        details += "\nSender Number : " + SENDER_NUMBER;
        model = getIntent().getParcelableExtra("data");
        details += "\nBeneficiary Name : " + model.getBenename();
        details += "\nAccount Number : " + model.getBeneaccount();
        details += "\nIFSC Code : " + model.getBeneifsc();
        details += "\nBank Name : " + model.getBenebank();
        tvDetails.setText(details);
        etAmount = findViewById(R.id.etAmount);
        btnProceed = findViewById(R.id.btnProceed);
        dataList = new ArrayList<>();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmt_money_transfer);
        init();
        btnProceed.setOnClickListener(v -> {
            if (isValid()) {
                showLoader(DMTTransaction.this, "Please confirm Bank, Account Number and amount, " +
                        "transfer will not reverse at any circumstances.");
            }
        });

        tvGenPin.setOnClickListener(v -> startActivity(new Intent(this, PinReset.class)));
    }

    private boolean isValid() {
        if (etAmount.getText().toString().equals("")) {
            Toast.makeText(this, "Amount field is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etTPin.getText().toString().equals("")) {
            Toast.makeText(this, "Pin field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            if (Double.parseDouble(etAmount.getText().toString()) < 100 ||
                    Double.parseDouble(etAmount.getText().toString()) > 25000) {
                Toast.makeText(this, "Amount should be between 100 to 25000 ", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            new VolleyPostNetworkCall(this, this, url, isLoad).netWorkCall(param());
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "transfer");
        map.put("mobile", SENDER_NUMBER);
        map.put("name", SENDER_NAME);
        map.put("benebank", model.getBenebankid());
        map.put("benebankName", model.getBenebank());
        map.put("beneifsc", model.getBeneifsc());
        map.put("benemobile", model.getBenemobile());
        map.put("beneaccount", model.getBeneaccount());
        map.put("txntype", "");
        map.put("benename", model.getBenename());
        map.put("beneid", model.getBeneid());
        map.put("amount", etAmount.getText().toString());
        map.put("pin", etTPin.getText().toString());
        return map;
    }

    @SuppressLint("SetTextI18n")
    private void showLoader(Activity context, String msg) {
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pay_confirmation_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        ImageView imgProvider = dialog.findViewById(R.id.imgProvider);
        imgProvider.setImageDrawable(getResources().getDrawable(R.drawable.ic_bank));
        TextView tvMobile = dialog.findViewById(R.id.tvMobile);
        TextView lbl = dialog.findViewById(R.id.lbl);
        lbl.setText(msg);
        tvMobile.setText(model.getBeneaccount());
        TextView tvOperator = dialog.findViewById(R.id.tvOperator);
        tvOperator.setText(model.getBenebank() + "\n" + MyUtil.formatWithRupee(this, etAmount.getText().toString()));
        ImageView imgEdit = dialog.findViewById(R.id.imgEdit);
        Button cancelBtn = dialog.findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(v -> closeLoader());
        imgEdit.setOnClickListener(v -> {
            closeLoader();
            finish();
        });
        Button confirmButton = dialog.findViewById(R.id.btnConfirm);
        confirmButton.setOnClickListener(v -> {
            closeLoader();
            networkCallUsingVolleyApi(urlString, true);
        });

        dialog.show();
    }

    private void closeLoader() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            //String status = AppHandler.getStatus(JSonResponse);
            String rootMessage = "";

            if (jsonObject.has("message")) {
                rootMessage = AppHandler.getMessage(JSonResponse);
            }

            rootStatus = AppHandler.getStatus(JSonResponse);
            String rrn = "", message = "", txnStatus = "";
            dataList.clear();

            if (jsonObject.has("data")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if (jsonObject1.has("rrn")) {
                        rrn = jsonObject1.getString("rrn");
                    } else {
                        rrn = "NA";
                    }
                    if (jsonObject1.has("message")) {
                        message = jsonObject1.getString("message");
                    } else {
                        message = "NA";
                    }
                    txnStatus = AppHandler.getStatusFromObj(jsonObject1);
                    dataList.add(new LocalInvoiceModel(rrn, message, txnStatus));

                    if (dataList.size() < 2) {
                        rootStatus = AppHandler.getStatusFromObj(jsonObject1);
                    }
                }
            }
            Constents.INVOICE_DATA = new ArrayList<>();
            Constents.INVOICE_DATA.add(new InvoiceModel("Sender Number", SENDER_NUMBER));
            Constents.INVOICE_DATA.add(new InvoiceModel("Sender Name", SENDER_NAME));
            Constents.INVOICE_DATA.add(new InvoiceModel("Beneficiary Id", model.getBeneid()));
            Constents.INVOICE_DATA.add(new InvoiceModel("Beneficiary Name", model.getBenename()));
            Constents.INVOICE_DATA.add(new InvoiceModel("Beneficiary Number", model.getBenemobile()));
            Constents.INVOICE_DATA.add(new InvoiceModel("Account No", model.getBeneaccount()));
            Constents.INVOICE_DATA.add(new InvoiceModel("Bank", model.getBenebank()));
            Constents.INVOICE_DATA.add(new InvoiceModel("IFSC Code", model.getBeneifsc()));
            Constents.INVOICE_DATA.add(new InvoiceModel("Date", Constents.COMMON_DATE_FORMAT.format(new Date())));
            for (LocalInvoiceModel model : dataList) {
                Constents.INVOICE_DATA.add(new InvoiceModel("------------------", "-----------------------------"));
                Constents.INVOICE_DATA.add(new InvoiceModel("RRN", model.getRrn()));
                Constents.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(this, etAmount.getText().toString())));
                Constents.INVOICE_DATA.add(new InvoiceModel("Status", model.getStatus()));
                Constents.INVOICE_DATA.add(new InvoiceModel("Message", model.getMessage()));
            }
            Constents.INVOICE_DATA.add(new InvoiceModel("------------------", "-----------------------------"));
            Intent i = new Intent(this, ReportInvoice.class);
            i.putExtra("remark", "" + rootMessage);
            if (dataList.size() > 1) {
                i.putExtra("status", "no");
            } else {
                i.putExtra("status", rootStatus);
            }
            i.putExtra("invoiceType", "dmt");
            startActivity(i);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }
}
