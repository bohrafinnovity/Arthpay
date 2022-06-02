package com.payment.arthpay.activity;

import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.payment.arthpay.R;
import com.payment.arthpay.adapter.TransactionReportsCardAdapter;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.model.TransactionReportsItems;
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

public class TransactionReports extends AppCompatActivity {
    SwipeRefreshLayout swiprefresh_aeps_report;
    RecyclerView recyclerview_eaps_transaction_reports;
    TextView textview_message;
    List<TransactionReportsItems> transactionReportsItems, filterData;
    TransactionReportsCardAdapter transactionReportsCardAdapter;

    ProgressDialog dialog;

    String type = "";


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
    private int reloadCount = 0;

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
                    if (AppManager.isOnline(TransactionReports.this)) {
                        start_page = 1;
                        swiped_refresh = false;
                        transactionReportsItems.clear();
                        textview_message.setVisibility(View.GONE);
                        recyclerview_eaps_transaction_reports.setVisibility(View.VISIBLE);

                        mGetTransactionReports(SharedPrefs.getValue(TransactionReports.this, SharedPrefs.APP_TOKEN),
                                SharedPrefs.getValue(TransactionReports.this, SharedPrefs.USER_ID), type);
                    } else {
                        textview_message.setVisibility(View.VISIBLE);
                        textview_message.setText("No internet connection");
                        recyclerview_eaps_transaction_reports.setVisibility(View.GONE);
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
            final AlertDialog.Builder adb = new AlertDialog.Builder(TransactionReports.this);
            final View view = LayoutInflater.from(TransactionReports.this).inflate(R.layout.date_picker, null);
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
            final AlertDialog.Builder adb = new AlertDialog.Builder(TransactionReports.this);
            final View view = LayoutInflater.from(TransactionReports.this).inflate(R.layout.date_picker, null);
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

        btnFilter.setOnClickListener(v -> {
            searchText = tvSearch.getText().toString();
            start_page = 1;
            transactionReportsItems.clear();
            transactionReportsCardAdapter.notifyDataSetChanged();
            if (AppManager.isOnline(TransactionReports.this)) {
                textview_message.setVisibility(View.GONE);
                swiped_refresh = false;
                recyclerview_eaps_transaction_reports.setVisibility(View.VISIBLE);
                isFilterData = true;
                mGetTransactionReports(SharedPrefs.getValue(TransactionReports.this, SharedPrefs.APP_TOKEN),
                        SharedPrefs.getValue(TransactionReports.this, SharedPrefs.USER_ID), type);
            } else {
                textview_message.setVisibility(View.VISIBLE);
                textview_message.setText("No internet connection");
                recyclerview_eaps_transaction_reports.setVisibility(View.GONE);
            }
        });
    }

    private void reloadListView(List<TransactionReportsItems> productLadgerList) {
        transactionReportsCardAdapter = new TransactionReportsCardAdapter(TransactionReports.this, productLadgerList);
        recyclerview_eaps_transaction_reports.setAdapter(transactionReportsCardAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_reports);
        transactionReportsItems = new ArrayList<>();
        init();
        start_page = 1;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        type = getIntent().getStringExtra("type");
        if (type.equalsIgnoreCase("aepsstatement")) {
            getSupportActionBar().setTitle("AEPS Transactions");
        } else if (type.equalsIgnoreCase("matmstatement")) {
            getSupportActionBar().setTitle("Matm Settlements");
        } else {
            getSupportActionBar().setTitle("AEPS Wallet Statement");
        }

        swiprefresh_aeps_report = findViewById(R.id.swiprefresh_aeps_report);
        recyclerview_eaps_transaction_reports = findViewById(R.id.recyclerview_eaps_transaction_reports);
        recyclerview_eaps_transaction_reports.setLayoutManager(new LinearLayoutManager(TransactionReports.this));

        transactionReportsCardAdapter = new TransactionReportsCardAdapter(TransactionReports.this, transactionReportsItems);
        recyclerview_eaps_transaction_reports.setAdapter(transactionReportsCardAdapter);
        textview_message = findViewById(R.id.textview_message);

        swiprefresh_aeps_report.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swiprefresh_aeps_report.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AppManager.isOnline(TransactionReports.this)) {
                    start_page = 1;
                    swiped_refresh = true;
                    transactionReportsItems.clear();
                    textview_message.setVisibility(View.GONE);
                    recyclerview_eaps_transaction_reports.setVisibility(View.VISIBLE);
                    mGetTransactionReports(SharedPrefs.getValue(TransactionReports.this, SharedPrefs.APP_TOKEN),
                            SharedPrefs.getValue(TransactionReports.this, SharedPrefs.USER_ID), type);

                } else {
                    swiprefresh_aeps_report.setRefreshing(false);
                    textview_message.setText("No internet connection");
                    textview_message.setVisibility(View.VISIBLE);
                    recyclerview_eaps_transaction_reports.setVisibility(View.GONE);
                }
            }
        });


        if (AppManager.isOnline(this)) {
            textview_message.setVisibility(View.GONE);
            recyclerview_eaps_transaction_reports.setVisibility(View.VISIBLE);

            mGetTransactionReports(SharedPrefs.getValue(TransactionReports.this, SharedPrefs.APP_TOKEN),
                    SharedPrefs.getValue(TransactionReports.this, SharedPrefs.USER_ID), type);
        } else {
            textview_message.setVisibility(View.VISIBLE);
            textview_message.setText("No internet connection");
            recyclerview_eaps_transaction_reports.setVisibility(View.GONE);
        }


    }


    private void mGetTransactionReports(final String apptoken, final String user_id, final String type) {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(TransactionReports.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
                swiprefresh_aeps_report.setRefreshing(true);
            }

            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url;
                    if (isFilterData) {
                        //fromdate, todate, searchtext, status
                        String u = Constents.URL.baseUrl + "api/android/transaction?apptoken=" +
                                apptoken + "" +
                                "&fromdate=" + fromDateString + "" +
                                "&todate=" + toDateString + "" +
                                "&searchtext=" + searchText + "" +
                                "&status=" + LEVEL1 + "" +
                                "&user_id=" + user_id + "" +
                                "&type=" + type + "&start=" + start_page + "&device_id=" + Constents.MOBILE_ID;
                        url = new URL(u);
                        Print.P(u);
                    } else {
                        url = new URL(Constents.URL.baseUrl + "api/android/transaction?apptoken=" +
                                apptoken + "&user_id=" + user_id + "&type=" + type + "&start=" + start_page + "&device_id=" + Constents.MOBILE_ID);
                        Print.P(Constents.URL.baseUrl + "api/android/transaction?apptoken=" +
                                apptoken + "&user_id=" + user_id + "&type=" + type + "&start=" + start_page);
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
                swiprefresh_aeps_report.setRefreshing(false);
                dialog.dismiss();
                Print.P("Aeps : " + result);
                if (!result.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("statuscode")) {
                            String status = jsonObject.getString("statuscode");
                            if (status.equalsIgnoreCase("UA")) {
                                AppManager.getInstance().logoutApp(TransactionReports.this);
                            } else if (status.equalsIgnoreCase("txn")) {
                                reloadCount = transactionReportsItems.size();
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    TransactionReportsItems items = new TransactionReportsItems();
                                    items.setId(data.getString("id"));
                                    items.setMobile(data.getString("mobile"));
                                    items.setAadhaar(data.getString("aadhar"));
                                    items.setTxnid(data.getString("txnid"));
                                    items.setAmount(data.getString("amount"));
                                    items.setCharge(data.getString("charge"));
                                    items.setBalance(data.getString("balance"));
                                    items.setStatus(data.getString("status"));
                                    items.setType(data.getString("type"));

                                    if (type.equalsIgnoreCase("aepsstatement")) {
                                        items.setTypeFrom("Aeps Statement");
                                    } else if (type.equalsIgnoreCase("matmstatement")) {
                                        items.setTypeFrom("Matm");
                                    } else {
                                        items.setTypeFrom("Aeps Wallet");
                                    }
                                    items.setCreated_at(data.getString("created_at"));

                                    transactionReportsItems.add(items);

                                    //applyDateFilter();
                                }

                                if (jsonArray.length() > 0)
                                    transactionReportsCardAdapter.notifyDataSetChanged();
                                last_array_empty = jsonArray.length() == 0;

                                if (start_page > 1) {
                                    recyclerview_eaps_transaction_reports.scrollToPosition(reloadCount);
                                }

                            }
                        }

                        if (transactionReportsItems.size() == 0) {
                            transactionReportsCardAdapter.notifyDataSetChanged();
                            Toast.makeText(TransactionReports.this, "Data not available", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    textview_message.setText("Something went wrong");
                    textview_message.setVisibility(View.VISIBLE);
                    recyclerview_eaps_transaction_reports.setVisibility(View.GONE);
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
            mGetTransactionReports(SharedPrefs.getValue(TransactionReports.this, SharedPrefs.APP_TOKEN),
                    SharedPrefs.getValue(TransactionReports.this, SharedPrefs.USER_ID), type);

        }
    }


}
