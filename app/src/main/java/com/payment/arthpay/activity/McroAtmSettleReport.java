package com.payment.arthpay.activity;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.payment.arthpay.R;
import com.payment.arthpay.adapter.MicroAtmAdapter;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.model.MatmSettlementModel;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.Print;
import com.payment.arthpay.utill.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class McroAtmSettleReport extends AppCompatActivity {
    private SwipeRefreshLayout swiprefresh_recharge_statement;
    private RecyclerView recyclerview_statement;
    private List<MatmSettlementModel> items;
    private MicroAtmAdapter cardAdapter;
    private TextView textview_message;
    private String activity = "", type = "";
    public static boolean last_array_empty = false;
    public static int start_page = 0;
    boolean swiped_refresh = false;

    private TextView fromDate, toDate, tvSearch, tvFilterButton;
    private Spinner spnrStatus;
    private Button btnFilter;
    private ArrayAdapter stateAdapter;
    private int requestFlag;
    private List<String> stateNameList = new ArrayList<>();
    private String LEVEL1 = "-1";
    private boolean isFilterData = false;
    private String fromDateString = "", toDateString = "", searchText = "";
    private View filterContainer;

    private void init() {
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);
        btnFilter = findViewById(R.id.btnFetch);
        spnrStatus = findViewById(R.id.spnrStatus);
        btnFilter = findViewById(R.id.btnFetch);
        tvSearch = findViewById(R.id.tvSearch);
        tvFilterButton = findViewById(R.id.tvFilterButton);
        filterContainer = findViewById(R.id.filterContainer);
        tvSearch.setText("");

        tvFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterContainer.isShown()) {
                    filterContainer.setVisibility(View.GONE);
                    tvFilterButton.setText("Open Filter");
                    isFilterData = false;
                    stateAdapter.notifyDataSetChanged();
                    tvSearch.setText("");
                    searchText = "";
                    items.clear();
                    swiped_refresh = false;
                    start_page = 1;
                    if (AppManager.isOnline(McroAtmSettleReport.this)) {
                        start_page = 1;
                        mGetStatementData(SharedPrefs.getValue(McroAtmSettleReport.this, SharedPrefs.APP_TOKEN),
                                SharedPrefs.getValue(McroAtmSettleReport.this, SharedPrefs.USER_ID), type);
                        textview_message.setVisibility(View.GONE);
                        recyclerview_statement.setVisibility(View.VISIBLE);
                    } else {
                        textview_message.setText("No internet connection");
                        textview_message.setVisibility(View.VISIBLE);
                        recyclerview_statement.setVisibility(View.GONE);
                        swiprefresh_recharge_statement.setRefreshing(false);
                    }
                } else {
                    filterContainer.setVisibility(View.VISIBLE);
                    tvFilterButton.setText("Close Filter");
                }
            }
        });

        stateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateNameList);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnrStatus.setAdapter(stateAdapter);

        //accept, pending, success, reversed, refunded,failed
        stateNameList.add("Select Status");
        stateNameList.add("accept");
        stateNameList.add("pending");
        stateNameList.add("success");
        stateNameList.add("reversed");
        stateNameList.add("refunded");
        stateNameList.add("failed");
        stateAdapter.notifyDataSetChanged();

        spnrStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //Log.e("STATE SELECTED", "onItemSelected: " + stateNameList.get(position + 1));
                if (position > 0) {
                    LEVEL1 = stateNameList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                System.out.println("nothing is selected");

            }
        });

        fromDate.setOnClickListener(arg0 -> {
            final AlertDialog.Builder adb = new AlertDialog.Builder(McroAtmSettleReport.this);
            final View view = LayoutInflater.from(McroAtmSettleReport.this).inflate(R.layout.date_picker, null);
            adb.setView(view);
            final Dialog dialog;
            adb.setPositiveButton("Add",
                    (dialog1, arg1) -> {
                        DatePicker etDatePicker = view.findViewById(R.id.datePicker1);
                        //etDatePicker.setMaxDate(System.currentTimeMillis() + 1000);
                        Calendar cal = GregorianCalendar.getInstance();
                        cal.set(etDatePicker.getYear(), etDatePicker.getMonth(), etDatePicker.getDayOfMonth());
                        Date date = null;
                        date = cal.getTime();
                        String approxDateString = Constents.SHOWING_DATE_FORMAT.format(date);
                        fromDate.setText(approxDateString);
                        fromDateString = approxDateString;
                    });
            dialog = adb.create();
            dialog.show();
        });

        toDate.setOnClickListener(arg0 -> {
            final AlertDialog.Builder adb = new AlertDialog.Builder(McroAtmSettleReport.this);
            final View view = LayoutInflater.from(McroAtmSettleReport.this).inflate(R.layout.date_picker, null);
            adb.setView(view);
            final Dialog dialog;
            adb.setPositiveButton("Add",
                    (dialog1, arg1) -> {
                        DatePicker etDatePicker = view.findViewById(R.id.datePicker1);
                        //etDatePicker.setMaxDate(System.currentTimeMillis() + 1000);
                        Calendar cal = GregorianCalendar.getInstance();
                        cal.set(etDatePicker.getYear(), etDatePicker.getMonth(), etDatePicker.getDayOfMonth());
                        Date date = null;
                        date = cal.getTime();
                        String approxDateString = Constents.SHOWING_DATE_FORMAT.format(date);
                        toDate.setText(approxDateString);
                        toDateString = approxDateString;
                    });
            dialog = adb.create();
            dialog.show();
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = tvSearch.getText().toString();
                items.clear();
                cardAdapter.notifyDataSetChanged();
                swiped_refresh = false;
                start_page = 1;
                isFilterData = true;
                if (AppManager.isOnline(McroAtmSettleReport.this)) {
                    start_page = 1;
                    mGetStatementData(SharedPrefs.getValue(McroAtmSettleReport.this, SharedPrefs.APP_TOKEN),
                            SharedPrefs.getValue(McroAtmSettleReport.this, SharedPrefs.USER_ID), type);
                    textview_message.setVisibility(View.GONE);
                    recyclerview_statement.setVisibility(View.VISIBLE);
                } else {
                    textview_message.setText("No internet connection");
                    textview_message.setVisibility(View.VISIBLE);
                    recyclerview_statement.setVisibility(View.GONE);
                    swiprefresh_recharge_statement.setRefreshing(false);
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);

        items = new ArrayList<>();
        start_page = 1;
        init();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swiprefresh_recharge_statement = findViewById(R.id.swiprefresh_recharge_statement);

        recyclerview_statement = findViewById(R.id.recyclerview_statement);
        recyclerview_statement.setLayoutManager(new LinearLayoutManager(McroAtmSettleReport.this));

        cardAdapter = new MicroAtmAdapter(McroAtmSettleReport.this, items);
        recyclerview_statement.setAdapter(cardAdapter);

        textview_message = findViewById(R.id.textview_message);

        activity = getIntent().getStringExtra("activity");

        type = "matmfundrequest";
        getSupportActionBar().setTitle("MATM Settlements");

        if (AppManager.isOnline(this)) {
            mGetStatementData(SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN),
                    SharedPrefs.getValue(this, SharedPrefs.USER_ID), type);
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        swiprefresh_recharge_statement.setColorSchemeResources(R.color.colorPrimary);
        swiprefresh_recharge_statement.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AppManager.isOnline(McroAtmSettleReport.this)) {
                    start_page = 1;
                    swiped_refresh = true;
                    mGetStatementData(SharedPrefs.getValue(McroAtmSettleReport.this, SharedPrefs.APP_TOKEN),
                            SharedPrefs.getValue(McroAtmSettleReport.this, SharedPrefs.USER_ID), type);
                    textview_message.setVisibility(View.GONE);
                    recyclerview_statement.setVisibility(View.VISIBLE);
                } else {
                    textview_message.setText("No internet connection");
                    textview_message.setVisibility(View.VISIBLE);
                    recyclerview_statement.setVisibility(View.GONE);
                    swiprefresh_recharge_statement.setRefreshing(false);
                }
            }
        });
    }

    private void mGetStatementData(final String appToken, final String user_id, final String type) {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (swiprefresh_recharge_statement.isRefreshing()) {

                } else {
                    swiprefresh_recharge_statement.setRefreshing(true);
                }

            }

            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url;
                    if (isFilterData) {
                        //fromdate, todate, searchtext, status
                        String u = Constents.URL.baseUrl + "api/android/transaction?apptoken=" +
                                appToken + "" +
                                "&fromdate=" + fromDateString + "" +
                                "&todate=" + toDateString + "" +
                                "&searchtext=" + searchText + "" +
                                "&status=" + LEVEL1 + "" +
                                "&user_id=" + user_id + "" +
                                "&type=" + type + "&start=" + start_page;
                        url = new URL(u);
                        Print.P("Print Url: "+u);
                    } else {
                        url = new URL(Constents.URL.baseUrl + "api/android/transaction?apptoken=" +
                                appToken + "&user_id=" + user_id + "&type=" + type + "&start=" + start_page);
                    }
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
                return result.toString();
            }

            @Override
            protected void onPostExecute(String result) {

                //Do something with the JSON string
                if (swiprefresh_recharge_statement.isRefreshing()) {
                    swiprefresh_recharge_statement.setRefreshing(false);
                }
              //  System.out.print("Statement response : " + result);

                if (!result.equals("")) {

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("statuscode")) {
                            if (jsonObject.getString("statuscode").equalsIgnoreCase("txn")) {
                                if (swiped_refresh) {
                                    //Log.e("Log", "::::::::::::: Items Clear");
                                    items.clear();
                                }

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    MatmSettlementModel item = new MatmSettlementModel();

                                    item.setId(data.getString("id"));
                                    item.setMode(data.getString("mode"));
                                    item.setAmount(data.getString("amount"));
                                    item.setStatus(data.getString("status"));
                                    item.setType(data.getString("type"));
                                    item.setUserId(data.getString("user_id"));
                                    item.setCreatedAt(data.getString("created_at"));
                                    item.setType(data.getString("type"));
                                    item.setUsername(data.getString("username"));
                                    //item.setActivity(activity);

                                    items.add(item);

                                }

                                cardAdapter.notifyDataSetChanged();

                                if (jsonArray.length() != 0) {
                                    last_array_empty = false;
                                } else {
                                    last_array_empty = true;
                                }
                            } else {
                                recyclerview_statement.setVisibility(View.GONE);
                                textview_message.setText("Something went wrong");
                                textview_message.setVisibility(View.VISIBLE);
                            }
                        }

                        if (items.size() == 0) {
                            textview_message.setText("No records found");
                            recyclerview_statement.setVisibility(View.GONE);
                            textview_message.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    textview_message.setText("Something went wrong");
                    recyclerview_statement.setVisibility(View.GONE);
                    textview_message.setVisibility(View.VISIBLE);
                }
            }
        }

        getJSONData getJSONData = new getJSONData();
        getJSONData.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void mCallNextList() {
        start_page += 1;

        if (AppManager.isOnline(this)) {
            mGetStatementData(SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN),
                    SharedPrefs.getValue(this, SharedPrefs.USER_ID), type);

        }
    }


}
