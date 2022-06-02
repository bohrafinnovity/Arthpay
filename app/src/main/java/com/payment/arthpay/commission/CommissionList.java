package com.payment.arthpay.commission;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.commission.gsonModel.CommissionModel;
import com.payment.arthpay.httpRequest.VolleyPostNetworkCall;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommissionList extends AppCompatActivity implements VolleyPostNetworkCall.RequestResponseLis {
    ArrayList<String> keyList;
    ListView listView;
    private AlertDialog loaderDialog;
    private CommissionServiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commition_list);
        Constents.commissionDataList = new ArrayList<>();
        initListView();

        networkCallUsingVolleyApi(Constents.URL.COMMISION, true);
    }

    private void initListView() {
        keyList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        adapter = new CommissionServiceAdapter(this, keyList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CommissionList.this, CommissionDataList.class);
                intent.putExtra("key", keyList.get(i));
                startActivity(intent);
            }
        });
    }

    private void closeLoader() {
        if (loaderDialog != null && loaderDialog.isShowing()) {
            loaderDialog.dismiss();
        }
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
        return map;
    }


    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P("LOG : "+ JSonResponse);
        Constents.commissionDataList.clear();
        keyList.clear();
        Gson gson = new GsonBuilder().create();
        closeLoader();
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            JSONObject commissionObj = jsonObject.getJSONObject("commission");
            Iterator<String> keys = commissionObj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray keyArray = commissionObj.getJSONArray(key);
                for (int i = 0; i < keyArray.length(); i++) {
                    JSONObject obj = keyArray.getJSONObject(i);
                    CommissionModel model = gson.fromJson(obj.toString(), CommissionModel.class);
                    model.setKeys(key);
                    Constents.commissionDataList.add(model);
                }
                keyList.add(key);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
        Toast.makeText(this, "Error: " + msg, Toast.LENGTH_SHORT).show();
    }
}