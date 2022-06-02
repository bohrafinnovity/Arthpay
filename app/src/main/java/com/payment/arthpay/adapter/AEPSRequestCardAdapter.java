package com.payment.arthpay.adapter;

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
import com.payment.arthpay.activity.AEPSRequests;
import com.payment.arthpay.activity.ShareReciept;
import com.payment.arthpay.model.AEPSRequestItems;
import com.payment.arthpay.model.ReciptModel;
import com.payment.arthpay.utill.AppManager;

import java.util.List;

public class AEPSRequestCardAdapter extends RecyclerView.Adapter<AEPSRequestCardAdapter.ViewHolder> {

    Context context;
    private int dataCount = 0;
    ReciptModel reciptModel;
    List<com.payment.arthpay.model.AEPSRequestItems> AEPSRequestItems;
    public AEPSRequestCardAdapter(Context context, List<AEPSRequestItems> AEPSRequestItems)
    {
        this.context=context;
        this.AEPSRequestItems = AEPSRequestItems;
    }


    @Override
    public int getItemCount() {
        return AEPSRequestItems ==null?0: AEPSRequestItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        AEPSRequestItems items= AEPSRequestItems.get(i);
        viewHolder.textview_id.setText("Id : "+items.getId());
        viewHolder.textview_amount.setText("Amount : Rs "+items.getAmount());
        viewHolder.textview_date.setText("Date : "+items.getCreated_at());
        viewHolder.textview_type.setText("Type : "+ AppManager.cL(items.getType()));
        viewHolder.textview_remark.setText("Remark : "+items.getRemark());
        viewHolder.textview_status.setText(AppManager.cL(items.getStatus()));
        dataCount = 6;
        if (items.getStatus().equalsIgnoreCase("success"))
        {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
        else if (items.getStatus().equalsIgnoreCase("failure")||items.getStatus().equalsIgnoreCase("fail")||items.getStatus().equalsIgnoreCase("failed"))
        {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.red));
        }
        else {
            viewHolder.textview_status.setBackgroundColor(context.getResources().getColor(R.color.orange));
        }

        if (i==AEPSRequestItems.size()-1) {
            if (AEPSRequests.last_array_empty) {

            } else {
                ((AEPSRequests)context).mCallNextList();
            }
        }
        viewHolder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReciptModel reciptModel = new ReciptModel();
                reciptModel.setField1("Id");
                reciptModel.setValue1(items.getId());
                reciptModel.setField2("Amount");
                reciptModel.setValue2(items.getAmount());
                reciptModel.setField3("Date");
                reciptModel.setValue3(items.getCreated_at());
                reciptModel.setField4("Type");
                reciptModel.setValue4(items.getType());
                reciptModel.setField5("Remark");
                reciptModel.setValue5(items.getRemark());
                reciptModel.setField6("Status");
                reciptModel.setValue6(items.getStatus());
                Toast.makeText(context, "Share receipt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ShareReciept.class);
                intent.putExtra("dataModel",reciptModel);
                intent.putExtra("dataCount",dataCount);
                intent.putExtra("from","AEPS");
                context.startActivity(intent);
            }
        });

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.aeps_request_reports_items,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textview_id,textview_amount,textview_type,textview_date,textview_remark,textview_status;
        ImageView imgShare;
        ViewHolder (View view)
        {
            super(view);
            textview_id=view.findViewById(R.id.textview_id);
            textview_amount=view.findViewById(R.id.textview_amount);
            textview_type=view.findViewById(R.id.textview_type);
            textview_date=view.findViewById(R.id.textview_date);
            textview_remark=view.findViewById(R.id.textview_remark);
            textview_status=view.findViewById(R.id.textview_status);
            imgShare=view.findViewById(R.id.imgShare);

        }
    }
}
