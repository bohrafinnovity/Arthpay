package com.payment.arthpay.RechargePlans;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.payment.arthpay.R;
import com.payment.arthpay.RechargePlans.adapter.PlanAdapter;
import com.payment.arthpay.RechargePlans.adapter.PlanTitleAdapter;
import com.payment.arthpay.RechargePlans.model.DataItem;
import com.payment.arthpay.RechargePlans.model.GenericModel;
import com.payment.arthpay.RechargePlans.networkCall.VolleyNetworkCall;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.utill.Print;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewPlans extends AppCompatActivity implements VolleyNetworkCall.CallResponseLis,
        PlanTitleAdapter.TitleItemClick, PlanAdapter.ItemClick {

    private RecyclerView planTitleListView, planListView;
    private List<DataItem> planDataList, planDataListFilter;
    private List<String> titleList;
    private PlanAdapter adapterPlan;
    private PlanTitleAdapter adapterPlanTitle;
    BottomSheetBehavior sheetBehavior;
    private String planValue, planDetail;
    private String operatorValue, contactValue, validity;
    String type;

    private void init() {
        planTitleListView = findViewById(R.id.planTitleList);
        planListView = findViewById(R.id.planList);
        planDataList = new ArrayList<>();
        planDataListFilter = new ArrayList<>();
        titleList = new ArrayList<>();
        RelativeLayout bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        TextView forPlan = findViewById(R.id.forPlan);
        forPlan.setText(getIntent().getStringExtra("operatorName"));

        ImageView imgClose = findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Constents.GLOBAL_POSITION = 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_plans);
        type = getIntent().getStringExtra("type");
        contactValue = getIntent().getStringExtra("contact");
        operatorValue = getIntent().getStringExtra("operator");

        init();

        planTitleListView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        adapterPlanTitle = new PlanTitleAdapter(titleList, getApplicationContext(), this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        planTitleListView.setLayoutManager(horizontalLayoutManager);
        planTitleListView.setAdapter(adapterPlanTitle);

        //planListView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        planListView.setLayoutManager(layoutManager);

        String url = Constents.URL.baseUrl + "api/android/recharge/getplan";
        String type1 = getIntent().getStringExtra("type1");
        if (type1 != null && type1.length() > 0) {
            url = Constents.URL.baseUrl + "api/android/recharge/getoffer";
            TextView lbl = findViewById(R.id.lbl);
            lbl.setText("Browse Offer");
        }

        new VolleyNetworkCall(ViewPlans.this, this).netWorkCall(
                SharedPrefs.getValue(this, SharedPrefs.USER_ID),
                SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN), operatorValue, contactValue, url);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                TextView tvPlan = view.findViewById(R.id.tvPrice);
                TextView tvDetail = view.findViewById(R.id.tvDetail);
                Button btnSelect = view.findViewById(R.id.btnSelect);

                String p = ViewPlans.this.getString(R.string.rupee) + planValue;
                tvPlan.setText(p);
                if (type.equals("mobile"))
                    tvDetail.setText(planDetail + "\n\nValidity - " + validity);
                else
                    tvDetail.setText(planDetail + "\n\nPlan - " + validity);

                btnSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("amount", planValue);
                        setResult(121, returnIntent);
                        finish();
                    }
                });

                ImageView btnCancel = view.findViewById(R.id.imgCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findViewById(R.id.disableCon).setVisibility(View.GONE);
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    findViewById(R.id.disableCon).setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onSuccess(String res) {
        Print.P("Plan View: " + res);
        try {
            JSONObject jsonObjectMain = new JSONObject(res);
            String status = "";
            if (jsonObjectMain.has("status"))
                status = jsonObjectMain.getString("status");
            else
                status = jsonObjectMain.getString("statuscode");

            if (res.contains("Plan Not Available")) {
                Toast.makeText(this, "Plans are not available", Toast.LENGTH_SHORT).show();
            } else if (status.equalsIgnoreCase("success") || status.equalsIgnoreCase("TXN")) {
                GenericModel model = Handler.parse(jsonObjectMain);
                titleList.addAll(model.getKeyList());
                planDataList.addAll(model.getDataList());
                adapterPlanTitle.notifyDataSetChanged();
                reloadList(0);
            } else {
                if (jsonObjectMain.has("message")) {
                    String msg = jsonObjectMain.getString("message");
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Plans are not available", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Json response error", Toast.LENGTH_SHORT).show();
        }
    }

    void reloadList(int index) {
        if (titleList.size() > 0) {
            planDataListFilter.clear();
            String key = titleList.get(index);
            for (DataItem model : planDataList) {
                if (model.getParentText().equalsIgnoreCase(key)) {
                    planDataListFilter.add(model);
                }
            }
        } else {
            planDataListFilter.addAll(planDataList);
        }
        adapterPlan = new PlanAdapter(planDataListFilter, this, this, type);
        planListView.setAdapter(adapterPlan);
    }

    @Override
    public void ontitleItemClick(int position) {
        reloadList(position);
    }

    @Override
    public void onButtonSelect(int p, DataItem model) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("amount", model.getAmount());
        setResult(121, returnIntent);
        finish();
    }

    @Override
    public void onImgExpand(int p, DataItem model) {
        planValue = model.getAmount();
        planDetail = model.getDetail();
        validity = model.getValidity();
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            findViewById(R.id.disableCon).setVisibility(View.VISIBLE);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            findViewById(R.id.disableCon).setVisibility(View.GONE);
        }
    }
}
