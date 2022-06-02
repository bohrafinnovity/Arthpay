package com.payment.arthpay.bppsServices;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.bppsServices.adapter.BillFetchParamListAdapter;
import com.payment.arthpay.bppsServices.model.BillPayModel;
import com.payment.arthpay.bppsServices.model.ParameItem;
import com.payment.arthpay.httpRequest.DemoVolleyNetworkCall;
import com.payment.arthpay.httpRequest.UpdateBillService;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FetchBill extends AppCompatActivity implements DemoVolleyNetworkCall.RequestResponseLis,
        BillFetchParamListAdapter.GetListDataLis {

    Toolbar toolbar;
    private ListView listView;
    private List<ParameItem> dataList;
    private Button btnFetch;
    private BillFetchParamListAdapter adapter;
    private TextView tvTitle;
    private TextView tvTitleSub;
    private ImageView btnBack;
    String provider_id;
    private int REQUEST_TYPE = 0;

    private void init() {
        TextView tvWallet = findViewById(R.id.tvBalance);
        TextView tvAeps = findViewById(R.id.tvAeps);
        ImageView imgSync = findViewById(R.id.imgSync);
        String balance = getString(R.string.rupee) + " " + SharedPrefs.getValue(this, SharedPrefs.MAIN_WALLET);
        String aeps = getString(R.string.rupee) + " " + SharedPrefs.getValue(this, SharedPrefs.APES_BALANCE);
        tvWallet.setText(balance);
        tvAeps.setText(aeps);
        imgSync.setOnClickListener(v -> new UpdateBillService(this, tvWallet, tvAeps));

        toolbar = findViewById(R.id.toolbar);
        listView = findViewById(R.id.listView);
        dataList = new ArrayList<>();
        adapter = new BillFetchParamListAdapter(this, dataList, this);
        listView.setAdapter(adapter);
        btnFetch = findViewById(R.id.btnFetch);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitleSub = findViewById(R.id.tvTitleSub);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_fetch);

        init();

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    REQUEST_TYPE = 1;
                    String url = Constents.URL.baseUrl + "api/android/billpay/transaction";
                    networkCallUsingVolleyApi(url, dynamicParam());
                }
            }
        });

        provider_id = getIntent().getStringExtra("provider");
        String url = Constents.URL.baseUrl + "api/android/billpay/getprovider";
        REQUEST_TYPE = 0;
        networkCallUsingVolleyApi(url, providerParam(provider_id));
    }

    private void networkCallUsingVolleyApi(String api, Map<String, String> param) {
        Print.P("param:  " + api);
        if (AppManager.isOnline(this)) {
            new DemoVolleyNetworkCall(this, this, api).netWorkCall(param);
        } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> providerParam(String provider) {
        Map<String, String> map = new HashMap<>();
        map.put("provider_id", provider);
        Print.P("param:  " + new JSONObject(map).toString());
        return map;
    }

    private Map<String, String> dynamicParam() {
        Map<String, String> map = new HashMap<>();
        map.put("apptoken", SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN));
        map.put("user_id", SharedPrefs.getValue(this, SharedPrefs.USER_ID));
        map.put("type", "validate");
        map.put("provider_id", provider_id);

        String approxDateString = Constents.SHOWING_DATE_FORMAT.format(new Date());
        map.put("duedate", approxDateString);

        for (int i = 0; i < dataList.size(); i++) {
            String key = "number" + i;
            map.put(key, dataList.get(i).getFieldInputValue());
        }

        //Log.e("param : ", "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        Print.P("param:  " + new JSONObject(map).toString());
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Log.e("RES : ", "######################################################");
        Print.P("param:  " + JSonResponse);
        Gson gson = new GsonBuilder().create();
        try {
            if (REQUEST_TYPE == 0) {
                Constents.BILL_MODEL = gson.fromJson(JSonResponse, BillPayModel.class);
                tvTitle.setText(Constents.BILL_MODEL.getType().toUpperCase());
                tvTitleSub.setText(Constents.BILL_MODEL.getName());
                dataList.addAll(Constents.BILL_MODEL.getParame());
                adapter.notifyDataSetChanged();
            } else if (REQUEST_TYPE == 1) {
                String status = "", message = "";
                JSONObject jsonObject = new JSONObject(JSonResponse);
                if (jsonObject.has("statuscode")) {
                    status = jsonObject.getString("statuscode");
                }

                if (jsonObject.has("status")) {
                    status = jsonObject.getString("status");
                }

                if (jsonObject.has("message")) {
                    message = jsonObject.getString("message");
                }
                String TransactionId = "", biller_name = "", amount = "", selected_due_date = "";
                if (status.equalsIgnoreCase("txn")) {
                    if (jsonObject.has("data") && !jsonObject.getString("data").equals("")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        biller_name = data.getString("customername");
                        amount = data.getString("dueamount");
                        selected_due_date = data.getString("duedate");
                        if (data.has("TransactionId")) {
                            TransactionId = data.getString("TransactionId");
                        }
                    }
                    Toast.makeText(this, R.string.bill_fetched, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }

                Intent i = new Intent(FetchBill.this, BillActivityNew.class);
                i.putExtra("amount", amount);
                i.putExtra("billerName", biller_name);
                i.putExtra("dueDate", selected_due_date);
                i.putExtra("amount", amount);
                i.putExtra("transactionId", TransactionId);
                i.putExtra("provider_id", provider_id);
                startActivity(i);
            }
        } catch (Exception e) {
            findViewById(R.id.noData).setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        findViewById(R.id.noData).setVisibility(View.VISIBLE);
        Toast.makeText(this, "Error" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getRecords(int position, ParameItem item) {
        dataList.set(position, item);
    }

    public boolean isValid() {
        boolean flag = true;
        for (ParameItem model : dataList) {
            String inputValue = model.getFieldInputValue();
            if (inputValue == null || inputValue.length() == 0) {
                Toast.makeText(this, model.getParamname().toLowerCase() + " " + "is required", Toast.LENGTH_LONG).show();
                flag = false;
                break;
            }
        }
        return flag;
    }
}
