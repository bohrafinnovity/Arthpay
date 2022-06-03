package com.payment.arthpay.printer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payment.arthpay.R;
import com.payment.arthpay.app.AppController;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.model.MicroATMModel;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;
import com.payment.arthpay.utill.SharedPrefs;

import org.egram.aepslib.apiService.Body.PrintDataModel;

import java.util.ArrayList;

public class Invoice extends AppCompatActivity {

    ScrollView scrollView;
    TextView titlebar;
    Context context;
    MicroATMModel model;
    private ImageView imgCrossFinish;
    private ImageView imgShare;

    private View zigzagtop;
    private LinearLayout mainInvoice;
    private ImageView imgPrint;
    private ImageView imgTxnStatus;
    private TextView tvTxnStatus;
    private LinearLayout layoutInvoice;
    private TextView tvInvoicenumber;
    private TextView tvRefStanCode;
    private TextView tvReqTxn;
    private TextView tvClientRefId;
    private TextView tvVenderId;
    private TextView tvMid;
    private TextView tvTid;
    private LinearLayout layoutCardNumber;
    private TextView tvCardNumber;
    private TextView tvRRN;
    private LinearLayout layoutDate;
    private TextView tvDate;
    private TextView tvAmount;
    private LinearLayout layoutAmount;
    private TextView tvTxnAmount;
    private TextView tvMsgCumStatusCode;
    private String Statuscode;

    public void init() {

        imgCrossFinish = findViewById(R.id.imgCrossFinish);
        imgShare = findViewById(R.id.imgShare);
        scrollView = findViewById(R.id.scrollView);
        zigzagtop = findViewById(R.id.zigzagtop);
        mainInvoice = findViewById(R.id.mainInvoice);
        imgPrint = findViewById(R.id.imgPrint);
        imgTxnStatus = findViewById(R.id.imgTxnStatus);
        tvTxnStatus = findViewById(R.id.tvTxnStatus);
        layoutInvoice = findViewById(R.id.layoutInvoice);
        tvInvoicenumber = findViewById(R.id.tvInvoicenumber);
        tvRefStanCode = findViewById(R.id.tvRefStanCode);
        tvReqTxn = findViewById(R.id.tvReqTxn);
        tvClientRefId = findViewById(R.id.tvClientRefId);
        tvVenderId = findViewById(R.id.tvVenderId);
        tvMid = findViewById(R.id.tvMid);
        tvTid = findViewById(R.id.tvTid);
        layoutCardNumber = findViewById(R.id.layoutCardNumber);
        tvCardNumber = findViewById(R.id.tvCardNumber);
        tvRRN = findViewById(R.id.tvRRN);
        layoutDate = findViewById(R.id.layoutDate);
        tvDate = findViewById(R.id.tvDate);
        tvAmount = findViewById(R.id.tvAmount);
        layoutAmount = findViewById(R.id.layoutAmount);
        tvTxnAmount = findViewById(R.id.tvTxnAmount);
        tvMsgCumStatusCode = findViewById(R.id.tvMsgCumStatusCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_layout);
        init();
        context = Invoice.this;
        ImageView print = findViewById(R.id.imgPrint);
        ImageView share = findViewById(R.id.imgShare);
        model = getIntent().getParcelableExtra("data");

        //testInitial();

        if (model != null) {
            setInitial(model);
        }

        imgCrossFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k(model);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l();
            }
        });
    }

    private void testInitial() {
        Statuscode = "000";
        if (Statuscode.equals("999")) {
            layoutAmount.setVisibility(View.GONE);
            layoutDate.setVisibility(View.GONE);
            layoutCardNumber.setVisibility(View.GONE);
            layoutInvoice.setVisibility(View.GONE);

            imgTxnStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_pending));
            tvTxnStatus.setText("Transaction Pending");
            tvTxnStatus.setTextColor(getResources().getColor(R.color.matYellow));
            String remark = "Transaction Pending..";
            String statusCode = Statuscode;
            remark += "(" + statusCode + ")";
            tvMsgCumStatusCode.setText(remark);
            tvMsgCumStatusCode.setTextColor(getResources().getColor(R.color.matYellow));

        } else {
            String remark = "Transaction Success ";
            String statusCode = Statuscode;
            remark += "(" + statusCode + ")";
            tvMsgCumStatusCode.setText(remark);
        }


    }

    private void setInitial(MicroATMModel model) {

        if (model.getStatuscode().equals("00")) {
            tvInvoicenumber.setText(model.getInvoicenumber());
            tvAmount.setText(model.getAmount());
            tvDate.setText(model.getDate());
            //Card number
            tvCardNumber.setText(model.getCardno());

            String remark = model.getBankremarks();
            String statusCode = model.getStatuscode();
            remark += "(" + statusCode + ")";
            tvMsgCumStatusCode.setText(remark);
        } else {

            layoutAmount.setVisibility(View.GONE);
            layoutDate.setVisibility(View.GONE);
            layoutCardNumber.setVisibility(View.GONE);
            layoutInvoice.setVisibility(View.GONE);

            String remark = model.getBankremarks();
            imgTxnStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_pending));
            tvTxnStatus.setText(remark);
            tvTxnStatus.setTextColor(getResources().getColor(R.color.matYellow));

            String statusCode = model.getStatuscode();
            remark += "(" + statusCode + ")";
            tvMsgCumStatusCode.setText(remark);
            tvMsgCumStatusCode.setTextColor(getResources().getColor(R.color.matYellow));


        }

        tvClientRefId.setText(model.getClientrefid());
        tvMid.setText(model.getMid());
        tvTid.setText(model.getTid());
        tvRefStanCode.setText(model.getRefstan());

        if (model.getRequesttxn() != null && model.getRequesttxn().length() > 0)
            tvReqTxn.setText(model.getRequesttxn());
        else {
            RelativeLayout layout = findViewById(R.id.layoutRequestTxn);
            View view = findViewById(R.id.viewRequestTxn);
            setInvisible(layout, view);
        }

        if (model.getVendorid() != null && model.getVendorid().length() > 0)
            tvReqTxn.setText(model.getVendorid());
        else {
            RelativeLayout layout = findViewById(R.id.layoutVenderId);
            View view = findViewById(R.id.viewVenderId);
            setInvisible(layout, view);
        }


        tvClientRefId.setText(model.getClientrefid());

        tvTxnAmount.setText(model.getTxnamount());

        //RRN
        if (model.getRrn() != null && model.getRrn().length() > 0)
            tvReqTxn.setText(model.getRrn());
        else {
            RelativeLayout layout = findViewById(R.id.layoutRRN);
            View view = findViewById(R.id.viewRRN);
            setInvisible(layout, view);
        }

        volleyNetworkCall(model);
    }

    public void setInvisible(RelativeLayout layout, View view) {
        layout.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
    }

    public final void l() {
        new wd().NUL(this.scrollView, this.context);

    }

    public final void k(MicroATMModel model) {

        if (Build.VERSION.SDK_INT >= 19) {

            final PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

            final String string = "E Banker Document";

            String s = "EBanker";
            final ArrayList<PrintDataModel> list2;
            final ArrayList<PrintDataModel> list = list2 = new ArrayList<PrintDataModel>();
            if (model.getStatuscode().equals("00")) {

                list.add(new PrintDataModel("Invoice Number", model.getInvoicenumber()));
                list.add(new PrintDataModel("Card Number", model.getCardno()));
                list.add(new PrintDataModel("Date Time", model.getDate()));
                list.add(new PrintDataModel("Amount", model.getAmount()));
            }

            list.add(new PrintDataModel("Ref. Stan No.", model.getRefstan()));

            if (model.getRequesttxn() != null && model.getRequesttxn().length() > 0)
                list.add(new PrintDataModel("Request Txn", model.getRequesttxn()));

            list.add(new PrintDataModel("Client Ref Id", model.getClientrefid()));

            if (model.getVendorid() != null && model.getVendorid().length() > 0)
                list.add(new PrintDataModel("Vendor Id", model.getVendorid()));
            list.add(new PrintDataModel("MID", model.getMid()));
            list.add(new PrintDataModel("Terminal Id", model.getTid()));

            if (model.getRrn() != null && model.getRrn().length() > 0)
                list.add(new PrintDataModel("RRN", model.getRrn()));

            list.add(new PrintDataModel("Txn Amount", model.getTxnamount()));

//            final ArrayList<PrintDataModel> list3 = list2;
//            list3.add(new PrintDataModel("Customer Name", "bank Name"));
//            list3.add(new PrintDataModel("Reference Id", "bank Name"));
//            final ArrayList<PrintDataModel> list4 = list2;
//            list4.add(new PrintDataModel("RRN", "bank Name"));
            final PrintManager printManager2 = printManager;
            final String s2 = string;
            final ArrayList<PrintDataModel> list5 = list2;

            list5.add(new PrintDataModel("Remark", model.getBankremarks()));

//            list5.add(new PrintDataModel("Helpline - ", "Email id : " + "s@gmail.com" + ", Mobile : " + "923993993"));

            printManager2.print(s2, new ud(this, list2, s, "app_name"), null);

        } else {
            // new Bd().NUL((View)this.parentLayout, "Sorry, your device does not support this feature", qd.Id);
        }
    }

    // This is the piece of code to make api call
    // To udpate matm transactions on the floor
    ProgressDialog dialog;
    static int statusCode;
    static String dileveryRes;

    private void volleyNetworkCall(MicroATMModel model) {
        if (AppManager.isOnline(this)) {
            netWorkCall(model);
        } else {
            Toast.makeText(this, "Internet in your device is not properly functional that's why matm record " +
                    "are not updated on portal", Toast.LENGTH_LONG).show();
        }
    }

    public void netWorkCall(MicroATMModel model) {
        Gson gson = new GsonBuilder().create();
        String res = gson.toJson(model);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating balance ...");
        dialog.show();
        dialog.setCancelable(false);
        String apiUrl = Constents.URL.baseUrl + "api/android/secure/microatm/update?apptoken=" +
                SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN) + "&user_id=" + SharedPrefs.getValue(this, SharedPrefs.USER_ID) +
                "&response=" + res + "&device_id=" + Constents.MOBILE_ID;
        Print.P("Matm : " + apiUrl);

        StringRequest sendRequest = new StringRequest(0, apiUrl,
                Response -> {
                    Print.P("RES : " + Response);
                    closeLoader();
                    Toast.makeText(context, "Records Updated on server", Toast.LENGTH_SHORT).show();
                }, volleyError -> {
            closeLoader();
            onError(volleyError);
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                statusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            protected void deliverResponse(String response) {
                dileveryRes = response;
                //Log.e("Log", "Response : " + dileveryRes);
                super.deliverResponse(response);
            }
        };

        sendRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(sendRequest);
    }

    private void onError(VolleyError volleyError) {
        String errorMsg = "Due to some error these records are not updated on our server.";
        if (volleyError.getMessage() != null) {
            errorMsg += "\nError: " + volleyError.getMessage();
        }

        errorMsg += "\n\nStatus Code: " + volleyError.networkResponse.statusCode;
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void closeLoader() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
}


