package com.payment.arthpay.on_boarding.select_state_district;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.databinding.SearchWithListActivityBinding;
import com.payment.arthpay.httpRequest.VolleyPostNetworkCall;
import com.payment.arthpay.member.MyUtil;
import com.payment.arthpay.on_boarding.district_model.DistrictModel;
import com.payment.arthpay.on_boarding.district_model.state_model.StateModel;
import com.payment.arthpay.utill.AppHandler;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchWithListActivity extends AppCompatActivity implements VolleyPostNetworkCall.RequestResponseLis,
        SearchListAdapter.OnSearchListClickListner {

    SearchWithListActivityBinding binding;
    SearchListAdapter stateListAdapter;
    String type, stateId;
    int REQUEST_TYPE = 0;
    private RecyclerView listView;
    private EditText etSearch;
    private List<StateModel> dataList, dataListAll;
    private List<DistrictModel> districtDataList, districtDataListAll;

    private void init() {
        dataList = new ArrayList<>();
        dataListAll = new ArrayList<>();
        districtDataList = new ArrayList<>();
        districtDataListAll = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        if (type.equalsIgnoreCase("district")) {
            stateId = bundle.getString("stateId");
        }
        listView = binding.listView;
        etSearch = binding.etSearch;
        binding.icBack.setOnClickListener(v -> finish());
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SearchWithListActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (type.equalsIgnoreCase("state")) {
                    dataList = new ArrayList<>();
                    for (StateModel d : dataListAll) {
                        if (d.getStatename().toLowerCase().contains(s.toString().toLowerCase())) {
                            dataList.add(d);
                        }
                    }
                    stateListAdapter.UpdateList(dataList);
                } else if (type.equalsIgnoreCase("district")) {
                    districtDataList = new ArrayList<>();
                    for (DistrictModel d : districtDataListAll) {
                        if (d.getDistrictname().toLowerCase().contains(s.toString().toLowerCase())) {
                            districtDataList.add(d);
                        }
                    }
                    stateListAdapter.UpdateDistrictList(districtDataList);
                }
            }
        });

        if (type.equalsIgnoreCase("state")) {
            REQUEST_TYPE = 0;
            networkCallUsingVolleyApi(Constents.URL.STATE_LIST, true);
        } else if (type.equalsIgnoreCase("district")) {
            REQUEST_TYPE = 1;
            networkCallUsingVolleyApi(Constents.URL.DISTRICT_LIST, true);
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
        if (MyUtil.isNN(stateId))
            map.put("stateid", stateId);
        return map;
    }


    private void initList() {

        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setItemAnimator(new DefaultItemAnimator());
        if (type.equalsIgnoreCase("state")) {
            stateListAdapter = new SearchListAdapter(this, dataList, this, type);
            listView.setAdapter(stateListAdapter);
            stateListAdapter.UpdateList(dataList);
        }
        if (type.equalsIgnoreCase("district")) {
            stateListAdapter = new SearchListAdapter(districtDataList, this, type);
            listView.setAdapter(stateListAdapter);
            stateListAdapter.UpdateDistrictList(districtDataList);
        }
    }


    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P("State Data : " + JSonResponse);
        try {
            if (REQUEST_TYPE == 0) {
                JSONObject object = new JSONObject(JSonResponse);
                if (object.has("data")) {
                    JSONArray stateArray = object.getJSONArray("data");
                    dataList.addAll(AppHandler.parseStateList(this, stateArray));
                    Collections.sort(dataList, StateModel.comparator);
                    dataListAll.addAll(dataList);
                    initList();
                    if (dataList.size() == 0) {
                        errorView("Sorry Records are not available !");
                    } else {
                        listView.setVisibility(View.VISIBLE);
                        findViewById(R.id.noData).setVisibility(View.GONE);
                    }
                }
            }

            if (REQUEST_TYPE == 1) {
                JSONObject object = new JSONObject(JSonResponse);
                if (object.has("data")) {
                    JSONArray stateArray = object.getJSONArray("data");
                    districtDataList.addAll(AppHandler.parseDistrictList(this, stateArray));
                    Collections.sort(dataList, StateModel.comparator);
                    districtDataListAll.addAll(districtDataList);
                    initList();
                    if (districtDataList.size() == 0) {
                        errorView("Sorry Records are not available !");
                    } else {
                        listView.setVisibility(View.VISIBLE);
                        findViewById(R.id.noData).setVisibility(View.GONE);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P("OnFailRequest : " + msg);
        Toast.makeText(this, "On Failure", Toast.LENGTH_SHORT).show();
    }

    private void errorView(String string) {
        TextView tvMsg = findViewById(R.id.tvMsg);
        tvMsg.setText(string);
        listView.setVisibility(View.GONE);
        findViewById(R.id.noData).setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchItemClicked(int postion, String stateId, String stateName) {
//        ExtensionFunction.showToast(this,statecode);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("stateName", stateName);
        returnIntent.putExtra("stateId", stateId);
        setResult(100, returnIntent);
        finish();
//        startActivity(new Intent(this, OnBoardingActivity.class));
    }

    @Override
    public void onDistrictItemClicked(int postion, String districtname, String districtId) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("districtname", districtname);
        returnIntent.putExtra("districtId", districtId);
        setResult(101, returnIntent);
        finish();
    }
}
