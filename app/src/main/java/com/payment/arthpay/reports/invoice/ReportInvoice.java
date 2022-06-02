package com.payment.arthpay.reports.invoice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.airbnb.lottie.LottieAnimationView;
import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.reports.MyUtil;
import com.payment.arthpay.reports.invoice.model.InvoiceModel;
import com.payment.arthpay.reports.invoice.model.PrintDataModel;

import java.util.ArrayList;

public class ReportInvoice extends AppCompatActivity implements View.OnClickListener {
    final ArrayList<PrintDataModel> list2;
    final ArrayList<PrintDataModel> list = list2 = new ArrayList<>();
    String message;
    private NestedScrollView scrollView;
    private Context context;
    private ImageView imgShare, imgPrint;
    private LottieAnimationView imgTxnStatus;
    private TextView tvTxnStatus, tvValue;
    private RelativeLayout top;
    private LinearLayout invoiceCon;

    public void init() {
        imgShare = findViewById(R.id.imgShare);
        invoiceCon = findViewById(R.id.invoiceCon);
        top = findViewById(R.id.top);
        imgPrint = findViewById(R.id.imgPrint);
        scrollView = findViewById(R.id.scrollView);
        tvValue = findViewById(R.id.tvValue);
        imgTxnStatus = findViewById(R.id.imgTxnStatus);
        tvTxnStatus = findViewById(R.id.tvTxnStatus);
        setLis();
        permissionCheck();
        setupToolBar();
    }

    private void setupToolBar() {
        ImageView backImage = findViewById(R.id.imgClose);
        backImage.setOnClickListener(view -> finish());
    }

    private void setLis() {
        imgShare.setOnClickListener(this);
        imgPrint.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.transparentStatusBar(this);
        setContentView(R.layout.report_invoice);
        init();
        context = ReportInvoice.this;
        parseRes();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void parseRes() {
        try {
            message = getIntent().getStringExtra("remark");
            if (message == null || message.length() == 0 || message.trim().equalsIgnoreCase("null")) {
                message = "Not Available";
            }
            tvValue.setText(message);
            String status = getIntent().getStringExtra("status");
            switch (status) {
                case "success":
                    tvTxnStatus.setText(R.string.tran_success);
                    top.setBackgroundColor(getResources().getColor(R.color.green));
                    imgTxnStatus.setAnimation(R.raw.transaction_scuccess);
                    break;
                case "pending":
                    tvTxnStatus.setText(R.string.transaction_pending);
                    tvTxnStatus.setTextColor(getResources().getColor(R.color.text_yellow));
                    top.setBackgroundColor(getResources().getColor(R.color.bg_yellow));
                    imgTxnStatus.setAnimation(R.raw.payment_pending_file);
                    break;
                case "approved":
                    tvTxnStatus.setText(R.string.transaction_approved);
                    tvTxnStatus.setTextColor(getResources().getColor(R.color.textGreen));
                    top.setBackgroundColor(getResources().getColor(R.color.green));
                    imgTxnStatus.setAnimation(R.raw.transaction_scuccess);
                    break;
                case "rejected":
                    tvTxnStatus.setText(R.string.transaction_rejected);
                    tvTxnStatus.setTextColor(getResources().getColor(R.color.textRed));
                    top.setBackgroundColor(getResources().getColor(R.color.red));
                    imgTxnStatus.setAnimation(R.raw.payment_failed);
                    break;
                case "failed":
                case "failure":
                case "fail":
                case "Failed":
                    tvTxnStatus.setText(R.string.tran_failed);
                    tvTxnStatus.setTextColor(getResources().getColor(R.color.textRed));
                    top.setBackgroundColor(getResources().getColor(R.color.red));
                    imgTxnStatus.setAnimation(R.raw.payment_failed);
                    break;
                default:
                    tvTxnStatus.setText(String.format("%s %s", getString(R.string.transaction), status));
                    tvTxnStatus.setTextColor(getResources().getColor(R.color.text_yellow));
                    top.setBackgroundColor(getResources().getColor(R.color.bg_yellow));
                    imgTxnStatus.setAnimation(R.raw.payment_pending_file);
            }
            initTransactionList();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void initTransactionList() {
        try {
            InvoiceListAdapter adapter = new InvoiceListAdapter(this, Constents.INVOICE_DATA);
            ListView listView = findViewById(R.id.listView);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(context, "Invoice Generation Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public final void l() {
        if (Build.VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED
                && this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        } else
            new wd().NUL(this.invoiceCon, this.context);
    }

    public final void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED
                && this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }

    public final void k() {
        for (InvoiceModel m : Constents.INVOICE_DATA) {
            list.add(new PrintDataModel(m.getKey(), m.getValue()));
        }
        final PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        final String string = "E Banker Document";
        String s = "EBanker";

        list2.add(new PrintDataModel("Remark", message));
        printManager.print(string, new ud(this, list2, s, "app_name"), null);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgPrint) {
            k();
        } else if (view.getId() == R.id.imgShare) {
            l();
        }
    }
}


