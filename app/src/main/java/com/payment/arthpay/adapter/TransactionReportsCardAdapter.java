package com.payment.arthpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.arthpay.R;
import com.payment.arthpay.activity.CheckStatus;
import com.payment.arthpay.activity.ShareReciept;
import com.payment.arthpay.activity.TransactionReports;
import com.payment.arthpay.model.ReciptModel;
import com.payment.arthpay.model.TransactionReportsItems;
import com.payment.arthpay.utill.AppManager;

import java.util.List;

public class TransactionReportsCardAdapter extends RecyclerView.Adapter<TransactionReportsCardAdapter.ViewHolder> {

    Context context;

    ReciptModel reciptModel;
    private int dataCount = 0;

    List<TransactionReportsItems> transactionReportsItems;

    public TransactionReportsCardAdapter(Context context, List<TransactionReportsItems> transactionReportsItems) {
        this.context = context;
        this.transactionReportsItems = transactionReportsItems;
    }


    @Override
    public int getItemCount() {
        return transactionReportsItems == null ? 0 : transactionReportsItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        TransactionReportsItems items = transactionReportsItems.get(i);


        viewHolder.textview_txnid.setText("Txn Id : " + items.getTxnid());

        viewHolder.textview_mobile.setText("Mobile : " + items.getMobile());
        viewHolder.textview_bcid.setText("BC Id : " + items.getAadhaar());
        viewHolder.textview_amount.setText("Amount : Rs " + items.getAmount());
        viewHolder.textview_charge.setText("Charge : Rs " + items.getCharge());
        viewHolder.textview_balance.setText("Balance Rs : " + items.getBalance());
        viewHolder.textview_date.setText("Date : " + items.getCreated_at());
        //viewHolder.textview_via.setText("Via  : "+items.getVia());
        viewHolder.textview_type.setText("Type : " + AppManager.cL(items.getType()));
        viewHolder.textview_status.setText(AppManager.cL(items.getStatus()));

        if (items.getTypeFrom().equals("Aeps Statement")) {
            //viewHolder.btnCheckStatus.setVisibility(View.GONE);
            if (items.getStatus() != null && items.getStatus().length() > 0 && items.getStatus().equalsIgnoreCase("pending")) {
                viewHolder.btnCheckStatus.setVisibility(View.VISIBLE);
            } else {
                viewHolder.btnCheckStatus.setVisibility(View.GONE);
            }
            viewHolder.btnCheckStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, CheckStatus.class);
                    i.putExtra("typeValue", "aeps");
                    i.putExtra("id", items.getId());
                    context.startActivity(i);
                }
            });
        } else {
            viewHolder.btnCheckStatus.setVisibility(View.GONE);
        }


        if (items.getStatus().equalsIgnoreCase("success")) {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else if (items.getStatus().equalsIgnoreCase("failure") || items.getStatus().equalsIgnoreCase("fail") || items.getStatus().equalsIgnoreCase("failed")) {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.red));
        } else {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.orange));
        }

        if (i == transactionReportsItems.size() - 1) {
            if (TransactionReports.last_array_empty) {

            } else {
                ((TransactionReports) context).mCallNextList();
            }
        }
        viewHolder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReciptModel reciptModel = new ReciptModel();
                reciptModel.setField1("Txn Id");
                reciptModel.setValue1(items.getTxnid());
                reciptModel.setField2("Mobile");
                reciptModel.setValue2(items.getMobile());
                reciptModel.setField3("BC Id");
                reciptModel.setValue3(items.getAadhaar());
                reciptModel.setField4("Amount");
                reciptModel.setValue4(items.getAmount());
                reciptModel.setField5("Charge");
                reciptModel.setValue5(items.getCharge());
                reciptModel.setField6("Balance");
                reciptModel.setValue6(items.getBalance());
                reciptModel.setField7("Date");
                reciptModel.setValue7(items.getCreated_at());
                reciptModel.setField8("Type");
                reciptModel.setValue8(items.getType());
                reciptModel.setField9("Status");
                reciptModel.setValue9(items.getStatus());

                dataCount = 9;
                Toast.makeText(context, "Share receipt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ShareReciept.class);
                intent.putExtra("dataModel", reciptModel);
                intent.putExtra("dataCount", dataCount);
                intent.putExtra("from", items.getTypeFrom());
                context.startActivity(intent);
            }
        });

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.aeps_transaction_reports_items, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textview_txnid, textview_mobile, textview_bcid, textview_amount, textview_charge, textview_balance, textview_date,
                textview_via, textview_type, textview_status;
        ImageView imgShare;
        Button btnCheckStatus;

        ViewHolder(View view) {
            super(view);
            textview_txnid = view.findViewById(R.id.textview_txnid);
            textview_mobile = view.findViewById(R.id.textview_mobile);
            textview_bcid = view.findViewById(R.id.textview_bcid);
            textview_amount = view.findViewById(R.id.textview_amount);
            textview_charge = view.findViewById(R.id.textview_charge);
            textview_balance = view.findViewById(R.id.textview_balance);
            textview_date = view.findViewById(R.id.textview_date);
            textview_via = view.findViewById(R.id.textview_via);
            textview_type = view.findViewById(R.id.textview_type);
            textview_status = view.findViewById(R.id.textview_status);
            imgShare = view.findViewById(R.id.imgShare);
            btnCheckStatus = view.findViewById(R.id.btnCheckStatus);

        }
    }
}
