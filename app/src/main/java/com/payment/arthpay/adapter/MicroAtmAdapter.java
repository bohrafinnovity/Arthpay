package com.payment.arthpay.adapter;

import android.app.ProgressDialog;
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
import com.payment.arthpay.activity.McroAtmSettleReport;
import com.payment.arthpay.activity.ShareReciept;
import com.payment.arthpay.model.MatmSettlementModel;
import com.payment.arthpay.model.ReciptModel;
import com.payment.arthpay.utill.AppManager;

import java.util.List;

public class MicroAtmAdapter extends RecyclerView.Adapter<MicroAtmAdapter.ViewHolder> {
    Context context;
    ReciptModel reciptModel;
    private int dataCount = 0;
    List<MatmSettlementModel> items;
    ProgressDialog dialog;

    public MicroAtmAdapter(Context context, List<MatmSettlementModel> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        MatmSettlementModel item = items.get(i);

        viewHolder.textview_txnid.setText("Txnid : " + item.getId());
        viewHolder.textview_date.setText("Mode : " + item.getMode());
        viewHolder.textview_number.setText("Amount : " + "Rs " + item.getAmount());
        viewHolder.textview_amount.setText("Type : " + item.getType());
        viewHolder.textview_profit.setVisibility(View.GONE);
        viewHolder.textview_status.setText(AppManager.cL(item.getStatus()));
        viewHolder.textview_balance.setText("Date : " + item.getCreatedAt());
        viewHolder.textview_desc.setText("User : " + item.getUsername());

        if (item.getStatus().equalsIgnoreCase("success")) {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else if (item.getStatus().equalsIgnoreCase("failure") ||
                item.getStatus().equalsIgnoreCase("fail") ||
                item.getStatus().equalsIgnoreCase("failed")) {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.red));
        } else {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.orange));
        }

        if (i == items.size() - 1) {
            if (McroAtmSettleReport.last_array_empty) {

            } else {
                ((McroAtmSettleReport) context).mCallNextList();
            }
        }
        viewHolder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReciptModel reciptModel = new ReciptModel();
                reciptModel.setField1("Txn Id");
                reciptModel.setValue1(item.getId());
                reciptModel.setField2("Mode");
                reciptModel.setValue2(item.getMode());
                reciptModel.setField3("Amount");
                reciptModel.setValue3(item.getAmount());
                reciptModel.setField4("Type");
                reciptModel.setValue4(item.getType());
                reciptModel.setField5("Date");
                reciptModel.setValue5(item.getCreatedAt());
                reciptModel.setField6("User");
                reciptModel.setValue6(item.getUsername());
                reciptModel.setField7("Status");
                reciptModel.setValue7(item.getStatus());

                dataCount = 7;
                Toast.makeText(context, "Share receipt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ShareReciept.class);
                intent.putExtra("dataModel", reciptModel);
                intent.putExtra("dataCount", dataCount);
                intent.putExtra("from", "MATM");
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.statement_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textview_txnid, textview_date, textview_number, textview_amount, textview_profit,
                textview_charge, textview_desc, textview_via, textview_balance, textview_status;
        ImageView imgShare;
        Button button_check_status;

        ViewHolder(View view) {
            super(view);
            textview_txnid = view.findViewById(R.id.textview_txnid);
            textview_date = view.findViewById(R.id.textview_date);
            textview_number = view.findViewById(R.id.textview_number);
            textview_amount = view.findViewById(R.id.textview_amount);
            textview_profit = view.findViewById(R.id.textview_profit);
            textview_charge = view.findViewById(R.id.textview_charge);
            textview_desc = view.findViewById(R.id.textview_desc);
            textview_via = view.findViewById(R.id.textview_via);
            textview_balance = view.findViewById(R.id.textview_balance);
            textview_status = view.findViewById(R.id.textview_status);
            imgShare = view.findViewById(R.id.imgShare);

            button_check_status = view.findViewById(R.id.button_check_status);
            button_check_status.setVisibility(View.GONE);
        }
    }

}
