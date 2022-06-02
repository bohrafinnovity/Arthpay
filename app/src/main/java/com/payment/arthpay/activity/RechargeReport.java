package com.payment.arthpay.activity;

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

import com.payment.arthpay.R;
import com.payment.arthpay.adapter.BankListAdapter;
import com.payment.arthpay.adapter.OperatorListAdapter;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.httpRequest.VolleyGetNetworkCall;
import com.payment.arthpay.model.BankListItems;
import com.payment.arthpay.model.Operators_Items;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RechargeReport extends AppCompatActivity implements VolleyGetNetworkCall.RequestResponseLis {
    private String url, type;
    private ListView operatorListView;
    private List<Operators_Items> operatorDataList;
    private List<BankListItems> bankDataList;
    private OperatorListAdapter operatorListAdapter;
    private BankListAdapter bankListAdapter;
    private Context context;
    private EditText etSearch;

    private void init() {
        context = RechargeReport.this;
        operatorListView = findViewById(R.id.operatorList);
        etSearch = findViewById(R.id.etSearch);
        operatorDataList = new ArrayList<>();
        bankDataList = new ArrayList<>();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operator_list);

        init();

        type = getIntent().getStringExtra("type");

        if (type != null && type.equals("bank")) {
            url = Constents.URL.baseUrl + "api/android/edmt/transaction?apptoken=" + SharedPrefs.getValue
                    (RechargeReport.this, SharedPrefs.APP_TOKEN) + "&user_id=" + SharedPrefs.getValue(
                    RechargeReport.this, SharedPrefs.USER_ID) + "&type=getbank";
        } else {
            url = Constents.URL.baseUrl + "api/android/recharge/providers?type=" + type;
        }

        if (type != null && type.equals("bank")) {
            bankListAdapter = new BankListAdapter(this, bankDataList);
            operatorListView.setAdapter(bankListAdapter);
        } else {
            operatorListAdapter = new OperatorListAdapter(this, operatorDataList);
            operatorListView.setAdapter(operatorListAdapter);
        }

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
                if (operatorDataList != null) {
                    List<Operators_Items> temp = new ArrayList<>();
                    for (Operators_Items d : operatorDataList) {
                        if (d.getOperator_name().toLowerCase().contains(s.toString().toLowerCase())) {
                            temp.add(d);
                        }
                    }
                    operatorListAdapter.UpdateList(temp);
                }
            }
        });

        operatorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Operators_Items item = operatorDataList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("id", item.getOperator_id());
                returnIntent.putExtra("name", item.getOperator_name());
                setResult(100, returnIntent);
                finish();
            }
        });

        networkCallUsingVolleyApi(url);
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
    public void onSuccessRequest(String result) {
        closeLoader();
        if (!result.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("statuscode")) {
                    String status = jsonObject.getString("statuscode");
                    if (status.equalsIgnoreCase("UA")) {
                        AppManager.getInstance().logoutApp(RechargeReport.this);
                    } else if (status.equalsIgnoreCase("TXN")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);

                            if (!type.equals("bank")) {
                                Operators_Items operators_items = new Operators_Items();
                                operators_items.setOperator_id(data.getString("id"));
                                operators_items.setOperator_name(data.getString("name"));
                                operatorDataList.add(operators_items);
                                operatorListAdapter.notifyDataSetChanged();
                            } else {
                                BankListItems bankitem = new BankListItems();
                                bankitem.setId(data.getString("id"));
                                bankitem.setBank(data.getString("bankname"));
                                bankitem.setIfsc(data.getString("ifsc"));
                                bankitem.setBank_id(data.getString("bankid"));
                                bankDataList.add(bankitem);
                                bankListAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        getFragmentManager().popBackStack();
                    }
                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
