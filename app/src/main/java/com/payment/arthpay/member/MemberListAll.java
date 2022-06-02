package com.payment.arthpay.member;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.httpRequest.DemoVolleyNetworkCall;
import com.payment.arthpay.member.adapter.AllMemberAdapter;
import com.payment.arthpay.member.model.AppMember;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MemberListAll extends AppCompatActivity implements DemoVolleyNetworkCall.RequestResponseLis, AllMemberAdapter.ItemClick {

    private static final int REQUEST_COUNT = 0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ArrayList<AppMember> dataList;
    private RecyclerView listView;
    private AllMemberAdapter adapter;
    private int PAGE_COUNT = 1;
    private int MAX_PAGE_COUNT = 1;
    private String TYPE = "";
    private boolean loading = true;
    private Button btnAddMember;
    private int REQUEST_TYPE = 0;
    private String amount = "";
    private String remark = "";
    private String id = "";

    private void init() {
        TYPE = getIntent().getStringExtra("type");
        listView = findViewById(R.id.reportList);
        btnAddMember = findViewById(R.id.btnAddMember);
        dataList = new ArrayList<>();
        initReportSlider();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.explodeAnimation(this);
        setContentView(R.layout.activity_member_list_all);
        init();

        REQUEST_TYPE = 0;
        networkCallUsingVolleyApi(Constents.URL.ALL_MEMBERS, true);

        btnAddMember.setOnClickListener(v -> {
            startActivity(new Intent(MemberListAll.this, AddMember.class));
        });
    }

    private void initReportSlider() {
        adapter = new AllMemberAdapter(this, dataList, this);
        LinearLayoutManager mLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            if (PAGE_COUNT < MAX_PAGE_COUNT) {
                                PAGE_COUNT++;
                                loadMoreLoader(true);
                                REQUEST_TYPE = 0;
                                networkCallUsingVolleyApi(Constents.URL.REPORT, false);
                            }
                            loading = true;
                        }
                    }
                }
            }
        });
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        Print.P("url : " + url);
        if (AppManager.isOnline(this)) {
            new DemoVolleyNetworkCall(this, this, url).netWorkCall(param());
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("apptoken", SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN));
        map.put("user_id", SharedPrefs.getValue(this, SharedPrefs.USER_ID));

        switch (REQUEST_TYPE) {
            case 0:
                map.put("type", TYPE);
                map.put("start", String.valueOf(PAGE_COUNT));
                break;
            case 1:
                map.put("type", "return");
                break;
            case 2:
                map.put("type", "transfer");
                break;
        }
        if (MyUtil.isNN(amount))
            map.put("amount", amount);
        if (MyUtil.isNN(remark))
            map.put("remark", remark);

        if (MyUtil.isNN(id))
            map.put("id", id);
        Print.P("param : " + new JSONObject(map));
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P("LOG : " + JSonResponse);
        loadMoreLoader(false);
        try {
            JSONObject object = new JSONObject(JSonResponse);
            String status;
            if (object.has("statuscode")) {
                status = object.getString("statuscode");
            } else {
                status = object.getString("status");
            }
            if (status.equalsIgnoreCase("TXN")) {
                switch (REQUEST_TYPE) {
                    case 0:
                        if (object.has("data")) {
                            int oldDataSize = dataList.size();
                            JSONArray bannerArray = object.getJSONArray("data");
                            MAX_PAGE_COUNT = object.getInt("pages");
                            dataList.addAll(MyUtil.parseMemberRecord(this, bannerArray));
                            adapter.notifyDataSetChanged();
                            listView.setVerticalScrollbarPosition(oldDataSize);
                            if (dataList.size() == 0) {
                                errorView("Sorry Records are not available !");
                            } else {
                                listView.setVisibility(View.VISIBLE);
                                findViewById(R.id.noData).setVisibility(View.GONE);
                            }
                        }
                        break;
                    default:
                        String message = MyUtil.getMessage(JSonResponse);
                        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                }
            } else {
                String msg = object.getString("message");
                Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            errorView(MyUtil.parseExceptionMsg(e));
        }
    }

    @Override
    public void onFailRequest(String msg) {
        errorView(msg);
    }

    private void loadMoreLoader(boolean flag) {
        if (flag) {
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.loader).setVisibility(View.GONE);
        }
    }

    private void errorView(String string) {
        TextView tvMsg = findViewById(R.id.tvMsg);
        tvMsg.setText(string);
        listView.setVisibility(View.GONE);
        findViewById(R.id.noData).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constents.IS_RELOAD_REQUEST) {
            Constents.IS_RELOAD_REQUEST = false;
            PAGE_COUNT = 1;
            MAX_PAGE_COUNT = 1;
            dataList.clear();
            REQUEST_TYPE = 0;
            networkCallUsingVolleyApi(Constents.URL.REPORT, true);
        }
    }

    @Override
    public void clickRec(AppMember model) {
        REQUEST_TYPE = 1;
        id = model.getId();
        transferDialog("Return Fund");
    }

    @Override
    public void clickSend(AppMember model) {
        REQUEST_TYPE = 2;
        id = model.getId();
        transferDialog("Transfer Fund");
    }

    AlertDialog alertDialog;

    protected void transferDialog(String str) {
        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2 = inflater2.inflate(R.layout.member_fund_popup, null);
        final EditText etAmount = v2.findViewById(R.id.etAmount);
        final EditText etRemark = v2.findViewById(R.id.etRemark);
        final ImageView imgClose = v2.findViewById(R.id.imgClose);
        final Button btnSubmit = v2.findViewById(R.id.btnSubmit);
        btnSubmit.setText(str);
        btnSubmit.setOnClickListener(v -> {
            if (AppManager.isOnline(MemberListAll.this)) {
                if (etAmount.getText().toString().equals("")) {
                    Toast.makeText(this, "Amount field is required", Toast.LENGTH_SHORT).show();
                } else {
                    amount = etAmount.getText().toString();
                    remark = etRemark.getText().toString();
                    networkCallUsingVolleyApi(Constents.URL.FUND_REQUEST, true);
                }
            } else {
                Toast.makeText(MemberListAll.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(MemberListAll.this);
        builder2.setCancelable(false);
        builder2.setView(v2);
        alertDialog = builder2.create();
        imgClose.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }
}
