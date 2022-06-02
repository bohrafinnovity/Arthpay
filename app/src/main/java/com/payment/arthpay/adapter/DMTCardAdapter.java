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
import com.payment.arthpay.activity.DMTStatement;
import com.payment.arthpay.activity.ShareReciept;
import com.payment.arthpay.model.ReciptModel;
import com.payment.arthpay.model.StatementItems;
import com.payment.arthpay.utill.AppManager;

import java.util.List;

public class DMTCardAdapter extends RecyclerView.Adapter<DMTCardAdapter.ViewHolder> {

    Context context;
    //    ReciptModel reciptModel;
    private int dataCount = 0;
    List<StatementItems> statementItems;

    public DMTCardAdapter(Context context, List<StatementItems> statementItems) {
        this.context = context;
        this.statementItems = statementItems;
    }

    @Override
    public int getItemCount() {
        return statementItems == null ? 0 : statementItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        StatementItems items = statementItems.get(i);
        viewHolder.textview_id.setText("Id : " + items.getId());
        viewHolder.textview_txnid.setText("Txnid : " + items.getTxnid());
        viewHolder.textview_number.setText("Number : " + items.getNumber());
        viewHolder.textview_mobile.setText("Mobile : " + items.getMobile());
        viewHolder.textview_desc.setText("Desc : " + items.getDescription());
        viewHolder.textview_remark.setText("Remark : " + items.getRemark());
        viewHolder.textview_amount.setText("Amount : Rs " + items.getAmount());
        viewHolder.textview_via.setText("Created At : " + items.getCreated_at());
        viewHolder.textview_via.setVisibility(View.VISIBLE);
        viewHolder.textview_profit.setText("Profit : Rs " + items.getProfit() + ", Charge : Rs " + items.getCharge() + "\nGST : Rs " + items.getGst() + ", TDS : Rs " + items.getTds());
        viewHolder.textview_balance.setText("Balance : Rs " + items.getBalance());
        viewHolder.textview_trans_type.setText("Trans Type : " + AppManager.cL(items.getTrans_type()));
        viewHolder.textview_status.setText(AppManager.cL(items.getStatus()));
        viewHolder.button_check_status.setVisibility(View.VISIBLE);
        viewHolder.button_check_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CheckStatus.class);
                i.putExtra("typeValue", "money");
                i.putExtra("id", items.getId());
                context.startActivity(i);
            }
        });

        viewHolder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReciptModel reciptModel = new ReciptModel();
                reciptModel.setField1("Id");
                reciptModel.setValue1(items.getId());
                reciptModel.setField2("Txnid");
                reciptModel.setValue2(items.getTxnid());
                reciptModel.setField3("Number");
                reciptModel.setValue3(items.getNumber());
                reciptModel.setField4("Mobile");
                reciptModel.setValue4(items.getMobile());
                reciptModel.setField5("Amount");
                reciptModel.setValue5(items.getAmount());
                reciptModel.setField6("Date");
                reciptModel.setValue6(items.getCreated_at());
                reciptModel.setField7("Charge");
                reciptModel.setValue7(items.getCharge());
                reciptModel.setField8("GST");
                reciptModel.setValue8(items.getGst());
                reciptModel.setField9("TDS");
                reciptModel.setValue9(items.getTds());
                reciptModel.setField10("Trans Type");
                reciptModel.setValue10(items.getTrans_type());
                reciptModel.setField11("Status");
                reciptModel.setValue11(items.getStatus());

                dataCount = 11;
                Toast.makeText(context, "Share receipt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ShareReciept.class);
                intent.putExtra("dataModel", reciptModel);
                intent.putExtra("dataCount", dataCount);
                intent.putExtra("from", "DMT");
                context.startActivity(intent);
            }
        });


        if (items.getStatus().equalsIgnoreCase("success")) {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else if (items.getStatus().equalsIgnoreCase("failure") ||
                items.getStatus().equalsIgnoreCase("fail") ||
                items.getStatus().equalsIgnoreCase("failed")) {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.red));
        } else {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.orange));
        }


        if (i == statementItems.size() - 1) {
            if (DMTStatement.last_array_empty) {

            } else {
                ((DMTStatement) context).mCallNextList();
            }
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pancard_statement_items, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textview_id, textview_txnid, textview_number, textview_mobile, textview_desc,
                textview_remark, textview_amount, textview_via, textview_profit, textview_balance, textview_trans_type, textview_status;
        ImageView imgShare;
        Button button_check_status;

        ViewHolder(View view) {
            super(view);
            textview_id = view.findViewById(R.id.textview_id);
            textview_txnid = view.findViewById(R.id.textview_txnid);
            textview_number = view.findViewById(R.id.textview_number);
            textview_mobile = view.findViewById(R.id.textview_mobile);
            textview_desc = view.findViewById(R.id.textview_desc);
            textview_remark = view.findViewById(R.id.textview_remark);
            textview_amount = view.findViewById(R.id.textview_amount);
            textview_via = view.findViewById(R.id.textview_via);
            textview_profit = view.findViewById(R.id.textview_profit);
            textview_balance = view.findViewById(R.id.textview_balance);
            textview_trans_type = view.findViewById(R.id.textview_trans_type);
            textview_status = view.findViewById(R.id.textview_status);
            button_check_status = view.findViewById(R.id.button_check_status);
            imgShare = view.findViewById(R.id.imgShare);
        }
    }
}
