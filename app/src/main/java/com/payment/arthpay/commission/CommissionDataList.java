package com.payment.arthpay.commission;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.commission.gsonModel.CommissionModel;

import java.util.ArrayList;

public class CommissionDataList extends AppCompatActivity {
    ArrayList<CommissionModel> dataList;
    ListView listView;
    private AlertDialog loaderDialog;
    private CommissionDataAdapter adapter;
    private String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commition_list);
        dataList = new ArrayList<>();
        key = getIntent().getStringExtra("key");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Commission");
        }

        initListView();
        if (Constents.commissionDataList != null && Constents.commissionDataList.size() > 0) {
            new PrepareDataList().execute();
        } else {
            findViewById(R.id.tvNoData).setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

    }

    private void initListView() {
        listView = findViewById(R.id.listView);
        adapter = new CommissionDataAdapter(this, dataList);
        listView.setAdapter(adapter);
    }

    private class PrepareDataList extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommissionDataList.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {
            for (CommissionModel model : Constents.commissionDataList) {
                if (model.getKeys().equals(key)) {
                    dataList.add(model);
                }
            }
            return "merchantHash";
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            // Log.e("Demo", "Res: " + merchantHash);
            super.onPostExecute(merchantHash);
            progressDialog.dismiss();
            if (dataList.size() > 0) {
                findViewById(R.id.tvNoData).setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            } else {
                findViewById(R.id.tvNoData).setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        }
    }
}