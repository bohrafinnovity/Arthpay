package com.payment.arthpay.pancards;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.payment.arthpay.R;
import com.payment.arthpay.activity.ShareReciept;
import com.payment.arthpay.model.ReciptModel;
import com.payment.arthpay.utill.AppManager;

import java.util.List;

public class PanCardAdapter extends RecyclerView.Adapter<PanCardAdapter.ViewHolder> {

    Context context;
    ReciptModel reciptModel;
    private int dataCount = 0;
    List<PanCardStatementItems> panCardStatementItems;
    public PanCardAdapter (Context context, List<PanCardStatementItems> panCardStatementItems)
    {
        this.context=context;
        this.panCardStatementItems=panCardStatementItems;
    }
    @Override
    public int getItemCount() {
        return panCardStatementItems==null?0:panCardStatementItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PanCardStatementItems items=panCardStatementItems.get(i);
        viewHolder.textview_id.setText("Id : "+items.getId());
        viewHolder.textview_txnid.setText("Txnid : "+items.getTxnid());
        viewHolder.textview_number.setText("Number : "+items.getNumber());
        viewHolder.textview_mobile.setText("Mobile : "+items.getMobile());
        viewHolder.textview_desc.setText("Desc : "+items.getDescription());
        viewHolder.textview_remark.setText("Remark : "+items.getRemark());
        viewHolder.textview_amount.setText("Amount : Rs "+items.getAmount());
        viewHolder.textview_via.setText("Date : "+items.getCreated_at());
        viewHolder.textview_via.setVisibility(View.VISIBLE);
        viewHolder.textview_profit.setText("Profit : Rs "+items.getProfit()+", Charge : Rs "+items.getCharge()+"\nGST : Rs "+items.getGst()+", TDS : Rs "+items.getTds());
        viewHolder.textview_balance.setText("Balance : Rs "+items.getBalance());
        viewHolder.textview_trans_type.setText("Trans Type : "+items.getTrans_type());
        viewHolder.textview_status.setText(AppManager.cL(items.getStatus()));
        dataCount = 15;

        if (items.getStatus().equalsIgnoreCase("success"))
        {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
        else if (items.getStatus().equalsIgnoreCase("failure")||
                items.getStatus().equalsIgnoreCase("fail")||
                items.getStatus().equalsIgnoreCase("failed"))
        {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.red));
        }
        else {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.orange));
        }

        if (i==panCardStatementItems.size()-1) {
            if (PanCardStatement.last_array_empty) {

            } else {
                ((PanCardStatement)context).mCallNextList();
            }
        }
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
                reciptModel.setField5("Desc");
                reciptModel.setValue5(items.getDescription());
                reciptModel.setField6("Remark");
                reciptModel.setValue6(items.getRemark());
                reciptModel.setField7("Amount");
                reciptModel.setValue7(items.getAmount());
                reciptModel.setField8("Date");
                reciptModel.setValue8(items.getCreated_at());
                reciptModel.setField9("Profit");
                reciptModel.setValue9(items.getProfit());
                reciptModel.setField10("Charge");
                reciptModel.setValue10(items.getCharge());
                reciptModel.setField11("GST");
                reciptModel.setValue11(items.getGst());
                reciptModel.setField12("TDS");
                reciptModel.setValue12(items.getTds());
                reciptModel.setField13("Balance");
                reciptModel.setValue13(items.getBalance());
                reciptModel.setField14("Trans Type");
                reciptModel.setValue14(items.getTrans_type());
                reciptModel.setField15("Status");
                reciptModel.setValue15(items.getStatus());
                Toast.makeText(context, "Share receipt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ShareReciept.class);
                intent.putExtra("dataModel",reciptModel);
                intent.putExtra("dataCount",dataCount);
                intent.putExtra("from","PANCARD");
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pancard_statement_items,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textview_id,textview_txnid,textview_number,textview_mobile,textview_desc,
                textview_remark,textview_amount,textview_via,textview_profit,textview_balance,textview_trans_type,textview_status;
        ImageView imgShare;
        ViewHolder(View view)
        {
            super(view);
            textview_id=view.findViewById(R.id.textview_id);
            textview_txnid=view.findViewById(R.id.textview_txnid);
            textview_number=view.findViewById(R.id.textview_number);
            textview_mobile=view.findViewById(R.id.textview_mobile);
            textview_desc=view.findViewById(R.id.textview_desc);
            textview_remark=view.findViewById(R.id.textview_remark);
            textview_amount=view.findViewById(R.id.textview_amount);
            textview_via=view.findViewById(R.id.textview_via);
            textview_profit=view.findViewById(R.id.textview_profit);
            textview_balance=view.findViewById(R.id.textview_balance);
            textview_trans_type=view.findViewById(R.id.textview_trans_type);
            textview_status=view.findViewById(R.id.textview_status);
            imgShare=view.findViewById(R.id.imgShare);
        }
    }
}
