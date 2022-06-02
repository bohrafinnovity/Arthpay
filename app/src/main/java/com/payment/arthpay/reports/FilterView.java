package com.payment.arthpay.reports;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.utill.SharedPrefs;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FilterView extends AppCompatActivity implements View.OnClickListener {
    private TextView tvFromDate, tvToDate;
    private AppCompatButton filterButton;
    private ImageView imgClose;
    private EditText etSearch;
    private RadioButton rbSuccess, rbPending, rbFailed, rbAccept, rbReversed, rbRefunded;
    private String status = "success";

    private void init() {
        tvFromDate = findViewById(R.id.tvFromDate);
        etSearch = findViewById(R.id.etSearch);
        filterButton = findViewById(R.id.filterButton);
        tvToDate = findViewById(R.id.tvToDate);
        imgClose = findViewById(R.id.imgClose);
        rbSuccess = findViewById(R.id.rbSuccess);
        rbPending = findViewById(R.id.rbPending);
        rbFailed = findViewById(R.id.rbFailed);
        rbAccept = findViewById(R.id.rbAccept);
        rbReversed = findViewById(R.id.rbReversed);
        rbRefunded = findViewById(R.id.rbRefunded);
        setLis();
    }

    private void setLis() {
        tvFromDate.setOnClickListener(this);
        tvToDate.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        filterButton.setOnClickListener(this);
        rbSuccess.setOnClickListener(this);
        rbPending.setOnClickListener(this);
        rbFailed.setOnClickListener(this);
        rbAccept.setOnClickListener(this);
        rbReversed.setOnClickListener(this);
        rbRefunded.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.transparentStatusBar(this);
        setContentView(R.layout.report_filter_view);
        init();
    }

    private void datePopUp(int count) {
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        final View view = LayoutInflater.from(this).inflate(R.layout.date_picker, null);
        adb.setView(view);
        final Dialog dialog;
        adb.setPositiveButton("Add",
                (dialog1, arg1) -> {
                    DatePicker etDatePicker = view.findViewById(R.id.datePicker1);
                    Calendar cal = GregorianCalendar.getInstance();
                    cal.set(etDatePicker.getYear(), etDatePicker.getMonth(), etDatePicker.getDayOfMonth());
                    Date date = null;
                    date = cal.getTime();
                    String approxDateString = Constents.SHOWING_DATE_FORMAT.format(date);
                    switch (count) {
                        case 1:
                            tvFromDate.setText(approxDateString);
                            break;
                        case 2:
                            tvToDate.setText(approxDateString);
                    }
                });
        dialog = adb.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // overridePendingTransition(R.anim.nothing, R.anim.slide_up);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvFromDate:
                datePopUp(1);
                break;
            case R.id.tvToDate:
                datePopUp(2);
                break;
            case R.id.imgClose:
                finish();
                break;
            case R.id.filterButton:
                Constents.IS_RELOAD_REQUEST = true;
                SharedPrefs.setValue(this, SharedPrefs.FILTER_DATE_FROM, tvFromDate.getText().toString());
                SharedPrefs.setValue(this, SharedPrefs.FILTER_DATE_TO, tvToDate.getText().toString());
                SharedPrefs.setValue(this, SharedPrefs.REPORT_SEARCH_TEXT, etSearch.getText().toString());
                SharedPrefs.setValue(this, SharedPrefs.FILTER_STATUS, status);
                finish();
                break;
            case R.id.rbSuccess:
                manageRbActiveStatus(1);
                status = "success";
                break;
            case R.id.rbPending:
                manageRbActiveStatus(2);
                status = "pending";
                break;
            case R.id.rbFailed:
                manageRbActiveStatus(3);
                status = "failed";
                break;
            case R.id.rbAccept:
                manageRbActiveStatus(4);
                status = "accept";
                break;
            case R.id.rbReversed:
                manageRbActiveStatus(5);
                status = "reversed";
                break;
            case R.id.rbRefunded:
                manageRbActiveStatus(6);
                status = "refunded";
                break;
        }
    }

    private void manageRbActiveStatus(int i) {
        switch (i) {
            case 1:
                rbSuccess.setChecked(true);
                rbPending.setChecked(false);
                rbFailed.setChecked(false);
                rbAccept.setChecked(false);
                rbReversed.setChecked(false);
                rbRefunded.setChecked(false);
                break;
            case 2:
                rbSuccess.setChecked(false);
                rbPending.setChecked(true);
                rbFailed.setChecked(false);
                rbAccept.setChecked(false);
                rbReversed.setChecked(false);
                rbRefunded.setChecked(false);
                break;
            case 3:
                rbSuccess.setChecked(false);
                rbPending.setChecked(false);
                rbFailed.setChecked(true);
                rbAccept.setChecked(false);
                rbReversed.setChecked(false);
                rbRefunded.setChecked(false);
                break;
            case 4:
                rbSuccess.setChecked(false);
                rbPending.setChecked(false);
                rbFailed.setChecked(false);
                rbAccept.setChecked(true);
                rbReversed.setChecked(false);
                rbRefunded.setChecked(false);
                break;
            case 5:
                rbSuccess.setChecked(false);
                rbPending.setChecked(false);
                rbFailed.setChecked(false);
                rbAccept.setChecked(false);
                rbReversed.setChecked(true);
                rbRefunded.setChecked(false);
                break;
            case 6:
                rbSuccess.setChecked(false);
                rbPending.setChecked(false);
                rbFailed.setChecked(false);
                rbAccept.setChecked(false);
                rbReversed.setChecked(false);
                rbRefunded.setChecked(true);
                break;
        }
    }
}
