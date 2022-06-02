package com.payment.arthpay.bppsServices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.bppsServices.adapter.OperatorListAdapterNew;
import com.payment.arthpay.bppsServices.model.ProviderModel;
import com.payment.arthpay.httpRequest.VolleyGetNetworkCall;
import com.payment.arthpay.utill.AppManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OperatorListNew extends AppCompatActivity implements VolleyGetNetworkCall.RequestResponseLis {
    private ListView operatorListView;
    private List<ProviderModel> operatorDataList, filterList;
    private OperatorListAdapterNew adapterNew;
    private Context context;
    private EditText etSearch;
    private Toolbar toolbar;
    private String url, type;

    private void init() {
        context = OperatorListNew.this;
        operatorListView = findViewById(R.id.operatorList);
        etSearch = findViewById(R.id.etSearch);
        operatorDataList = new ArrayList<>();
        filterList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operator_list);

        setSupportActionBar(toolbar);

        init();

        url = Constents.URL.baseUrl + "api/android/recharge/providers?type=" + type;
        adapterNew = new OperatorListAdapterNew(this, filterList);
        operatorListView.setAdapter(adapterNew);

        if (type.equalsIgnoreCase("electricity")) {
            etSearch.setVisibility(View.VISIBLE);
        }

        if (type.equalsIgnoreCase("bank")) {
            etSearch.setVisibility(View.VISIBLE);
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterList = new ArrayList<>();
                for (ProviderModel d : operatorDataList) {
                    if (d.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        filterList.add(d);
                    }
                }
                adapterNew.UpdateList(filterList);
            }
        });

        operatorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(OperatorListNew.this, FetchBill.class);
                intent.putExtra("provider", filterList.get(i).getId());
                startActivity(intent);
            }
        });

        networkCallUsingVolleyApi(url);
    }

    private void networkCallUsingVolleyApi(String url) {
        if (AppManager.isOnline(this)) {
            showLoader(getString(R.string.loading_text));
            new VolleyGetNetworkCall(this, this, url).netWorkCall();
        } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
        }
    }

    private androidx.appcompat.app.AlertDialog loaderDialog;

    private void showLoader(String loaderMsg) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
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
    public void onSuccessRequest(String result) {
        Gson gson = new GsonBuilder().create();
        closeLoader();
        if (!result.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("statuscode").equalsIgnoreCase("TXN")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        ProviderModel model = gson.fromJson(data.toString(), ProviderModel.class);
                        operatorDataList.add(model);
                        filterList.add(model);
                    }
                    adapterNew.notifyDataSetChanged();
                } else {
                    if (jsonObject.has("message")) {
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
