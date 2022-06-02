package com.payment.arthpay.reports.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.arthpay.R;
import com.payment.arthpay.reports.MyUtil;
import com.payment.arthpay.reports.invoice.ReportInvoice;
import com.payment.arthpay.reports.model.AEPSFundReqReportModel;
import com.payment.arthpay.utill.AppHandler;

import java.util.List;

public class AEPSFundReqReportAdapter extends RecyclerView.Adapter<AEPSFundReqReportAdapter.MyViewHolder> {
    private Context mContext;
    private List<AEPSFundReqReportModel> dataList;
    private String basePath;

    public AEPSFundReqReportAdapter(Context mContext, List<AEPSFundReqReportModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_report_aeps_fund_req_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        AEPSFundReqReportModel model = dataList.get(position);
        holder.tvTxnId.setText(model.getId());
        if (MyUtil.isNN(model.getBank()))
            holder.tvBank.setText(model.getBank());
        else
            holder.tvBank.setText("Not Available");

        if (MyUtil.isNN(model.getPayType())) {
            holder.tvReqType.setText(model.getMode());
        } else {
            holder.tvReqType.setText("Not Available");
        }
        holder.TVAmount.setText(MyUtil.formatWithRupee(mContext, model.getAmount()));
        holder.tvType.setText(model.getType());
        holder.tvStatus.setText(model.getStatus());
        holder.tvDateTime.setText(model.getCreatedAt());
        if (model.getStatus().equalsIgnoreCase("approved")) {
            holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.success_border_green));
        } else {
            holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.primary_border_red));
        }

        holder.btnInvoice.setOnClickListener(v -> {
            AppHandler.initAepsFundRequestReportReportData(model, mContext);
            Intent i = new Intent(mContext, ReportInvoice.class);
            i.putExtra("status", model.getStatus());
            i.putExtra("remark", "" + model.getRemark());
            mContext.startActivity(i);
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTxnId, TVAmount, tvType, tvReqType, tvBank, tvStatus, tvDateTime;
        public ImageView btnInvoice, btnShare;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            tvTxnId = view.findViewById(R.id.tvTxnId);
            cardView = view.findViewById(R.id.cardView);
            TVAmount = view.findViewById(R.id.TVAmount);
            tvReqType = view.findViewById(R.id.tvReqType);
            TVAmount = view.findViewById(R.id.TVAmount);
            tvBank = view.findViewById(R.id.tvBank);
            tvType = view.findViewById(R.id.tvType);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvDateTime = view.findViewById(R.id.tvDateTime);
            btnInvoice = view.findViewById(R.id.btnInvoice);
            btnShare = view.findViewById(R.id.btnShare);
        }
    }

}