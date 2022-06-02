package com.payment.arthpay.reports;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.payment.arthpay.R;

public class AllReports extends AppCompatActivity {

    private String TITLE = "";
    private RecyclerView rvReport;
    private TabLayout tabWallet ;
    private void init() {
        TITLE = getIntent().getStringExtra("title");
        rvReport = findViewById(R.id.reportList);
        initToolbar();
    }

    private void initToolbar() {
        TextView tvTitle = findViewById(R.id.toolbarTitle);
        //tvTitle.setText(TITLE);
        ImageView imgBack = findViewById(R.id.imgClose);
        imgBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.all_report_list);
        //gridInitReport();
        init();
    }

   /* private void gridInitReport() {
        LinearLayoutManager nearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvReport.setLayoutManager(nearLayoutManager);
        rvReport.setItemAnimator(new DefaultItemAnimator());
        VerticalReportListAdapter reportAdapter = new VerticalReportListAdapter(AllReports.this, MainLocalData.getReportAllGridRecord());
        rvReport.setAdapter(reportAdapter);
        rvReport.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                rvReport, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position) {
                    case 0:
                        Intent i = new Intent(AllReports.this, AEPSTransaction.class);
                        i.putExtra("title", "AEPS Transactions");
                        i.putExtra("type", "aepsstatement");
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(AllReports.this, AEPSFundRequest.class);
                        i.putExtra("title", "AEPS Request Report");
                        i.putExtra("type", "aepsfundrequest");
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(AllReports.this, AEPSTransaction.class);
                        i.putExtra("title", "AEPS Wallet Statement");
                        i.putExtra("type", "awalletstatement");
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent(AllReports.this, MainWalletFundReqStatement.class);
                        i.putExtra("title", "Main Wallet Statement");
                        i.putExtra("type", "fundrequest");
                        startActivity(i);
                        break;
                    case 4:
                        i = new Intent(AllReports.this, BillRechargeTransaction.class);
                        i.putExtra("title", "Recharge Statement");
                        i.putExtra("type", "rechargestatement");
                        startActivity(i);
                        break;
                    case 5:
                        i = new Intent(AllReports.this, BillRechargeTransaction.class);
                        i.putExtra("title", "Bill Payment Statement");
                        i.putExtra("type", "billpaystatement");
                        startActivity(i);
                        break;
                    case 6:
                        i = new Intent(AllReports.this, DMTTransactionReport.class);
                        i.putExtra("title", "DMT Statement");
                        i.putExtra("type", "dmtstatement");
                        startActivity(i);
                        break;
                    case 7:
                        i = new Intent(AllReports.this, MATMFundRequest.class);
                        i.putExtra("title", "MATM Settlement");
                        i.putExtra("type", "matmfundrequest");
                        //startActivity(i);
                        Toast.makeText(AllReports.this, "Under process", Toast.LENGTH_SHORT).show();
                        break;
                    case 8:
                        i = new Intent(AllReports.this, AEPSTransaction.class);
                        i.putExtra("title", "MATM Transactions");
                        i.putExtra("type", "matmstatement");
                        startActivity(i);
                        break;
                    case 9:
                        i = new Intent(AllReports.this, UpiTransactionReport.class);
                        i.putExtra("title", "UPI Transactions");
                        i.putExtra("type", "upistatement");
                        startActivity(i);
                        break;
                    default:
                        Toast.makeText(AllReports.this, "Under Process", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }*/

}
