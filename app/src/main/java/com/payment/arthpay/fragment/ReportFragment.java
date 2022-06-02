package com.payment.arthpay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.payment.arthpay.R;
import com.payment.arthpay.adapter.ReportsListAdapter;
import com.payment.arthpay.reports.AEPSFundRequest;
import com.payment.arthpay.reports.AEPSTransaction;
import com.payment.arthpay.reports.AepsWalletStatement;
import com.payment.arthpay.reports.BillRechargeTransaction;
import com.payment.arthpay.reports.DMTTransactionReport;
import com.payment.arthpay.reports.MainWalletFundReqStatement;
import com.payment.arthpay.reports.WalletStatement;
import com.payment.arthpay.utill.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {
    private TabLayout tabWallet;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activtiy_all_reports, container, false);

        listView = view.findViewById(R.id.listView);
        tabWallet = view.findViewById(R.id.tabWallet);
        tabWallet.addTab(tabWallet.newTab().setText("Transaction History"));
        tabWallet.addTab(tabWallet.newTab().setText("Wallet Statement"));
        tabWallet.addTab(tabWallet.newTab().setText("Settlement Report"));
        tabWallet.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString()) {
                    case "Transaction History":
                        initListView(0);
                        break;
                    case "Wallet Statement":
                        initListView(1);
                        break;
                    case "Settlement Report":
                        initListView(2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        initListView(0);


        return view;
    }

    private void initListView(int Type) {
        String MatmFlag = SharedPrefs.getValue(getActivity(), SharedPrefs.MICRO_ATM_BALANCE);
        List<String> dataList = new ArrayList<>();
        dataList.add("Aeps Statement");
        dataList.add("Billpay Statement");
        dataList.add("Money Transfer Statement");
        dataList.add("Recharge Statement");
        if (MatmFlag != null && !MatmFlag.equalsIgnoreCase("no")) {
            dataList.add("MATM Statement");
        }
        if (Type == 1) {
            dataList.clear();
            dataList.add("Main Wallet");
            dataList.add("Aeps Wallet");
            if (MatmFlag != null && !MatmFlag.equalsIgnoreCase("no")) {
                dataList.add("MATM Wallet");
            }
        }

        if (Type == 2) {
            dataList.clear();
            dataList.add("Wallet Fund Request");
            dataList.add("AEPS Fund Request");
            if (MatmFlag != null && !MatmFlag.equalsIgnoreCase("no")) {
                dataList.add("MATM Fund Request");
            }
        }

        ReportsListAdapter adapter = new ReportsListAdapter(getActivity(), dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String v = dataList.get(position);
            Intent intent = null;
            switch (v) {
                case "Aeps Statement":
                    intent = new Intent(getActivity(), AEPSTransaction.class);
                    intent.putExtra("title", "AEPS Transactions");
                    intent.putExtra("type", "aepsstatement");
                    startActivity(intent);
                    break;
                case "Billpay Statement":
                    intent = new Intent(getActivity(), BillRechargeTransaction.class);
                    intent.putExtra("title", "Bill Payment Statement");
                    intent.putExtra("type", "billpaystatement");
                    startActivity(intent);
                    break;
                case "Money Transfer Statement":
                    intent = new Intent(getActivity(), DMTTransactionReport.class);
                    intent.putExtra("type", "dmtstatement");
                    intent.putExtra("title", "Money Transfer Statement");
                    startActivity(intent);
                    break;
                case "Recharge Statement":
                    intent = new Intent(getActivity(), BillRechargeTransaction.class);
                    intent.putExtra("type", "rechargestatement");
                    intent.putExtra("title", "Recharge Statement");
                    startActivity(intent);
                    break;
                case "MATM Statement":
                    intent = new Intent(getActivity(), AEPSTransaction.class);
                    intent.putExtra("type", "matmstatement");
                    intent.putExtra("title", "MATM Statement");
                    startActivity(intent);
                    break;
                case "Main Wallet":
                    intent = new Intent(getActivity(), WalletStatement.class);
                    intent.putExtra("type", "accountstatement");
                    intent.putExtra("title", "Main Wallet");
                    startActivity(intent);
                    break;
                case "Aeps Wallet":
                    intent = new Intent(getActivity(), AepsWalletStatement.class);
                    intent.putExtra("type", "awalletstatement");
                    intent.putExtra("title", "Aeps Wallet");
                    startActivity(intent);
                    break;
                case "MATM Wallet":
                    intent = new Intent(getActivity(), AepsWalletStatement.class);
                    intent.putExtra("type", "matmwalletstatement");
                    intent.putExtra("title", "MATM Wallet");
                    startActivity(intent);
                    break;
                case "Wallet Fund Request":
                    intent = new Intent(getActivity(), MainWalletFundReqStatement.class);
                    intent.putExtra("type", "fundrequest");
                    intent.putExtra("title", "Wallet Settlement Request");
                    startActivity(intent);
                    break;
                case "AEPS Fund Request":
                    intent = new Intent(getActivity(), AEPSFundRequest.class);
                    intent.putExtra("type", "aepsfundrequest");
                    intent.putExtra("title", "Aeps Settlement Request");
                    startActivity(intent);
                    break;
                case "MATM Fund Request":
                    intent = new Intent(getActivity(), AEPSFundRequest.class);
                    intent.putExtra("type", "matmfundrequest");
                    intent.putExtra("title", "MATM Settlement Report");
                    startActivity(intent);
            }
        });
    }
}