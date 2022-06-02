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
import com.payment.arthpay.reports.model.FundRequestModel;
import com.payment.arthpay.utill.AppHandler;

import java.util.List;

public class WalletFundReqAdapter extends RecyclerView.Adapter<WalletFundReqAdapter.MyViewHolder> {
    private Context mContext;
    private List<FundRequestModel> dataList;
    private String basePath;

    public WalletFundReqAdapter(Context mContext, List<FundRequestModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_report_fund_request_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        FundRequestModel model = dataList.get(position);
        holder.tvTxnId.setText(model.getId());
        holder.tvAmount.setText(MyUtil.formatWithRupee(mContext, model.getAmount()));
        holder.tvType.setText(model.getType());
        holder.tvStatus.setText(model.getStatus());
        holder.tvDateTime.setText(model.getPaydate());
        holder.tvRef.setText(model.getRefNo());
        switch (model.getStatus()) {
            case "success":
            case "Success":
            case "approved":
                holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.success_border_green));
                break;
            case "failed":
            case "failure":
            case "fail":
            case "Failed":
                holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.primary_border_red));
                break;
            default:
                holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.pending_border_orange));
        }

        holder.btnInvoice.setOnClickListener(v -> {
            AppHandler.initWalletFundReqReportData(model, mContext);
            Intent i = new Intent(mContext, ReportInvoice.class);
            i.putExtra("status", model.getStatus());
            i.putExtra("remark", "" + model.getRemark());
            mContext.startActivity(i);
        });

       /* holder.btnShare.setOnClickListener(v -> {
            AppHandler.printInvoiceFromAdapter(holder.cardView, mContext);
        });*/
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTxnId, tvAmount, tvType, tvRef, tvDateTime, tvStatus;
        public ImageView btnInvoice, btnShare;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            tvTxnId = view.findViewById(R.id.tvTxnId);
            cardView = view.findViewById(R.id.cardView);
            tvRef = view.findViewById(R.id.tvRef);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvType = view.findViewById(R.id.tvType);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvDateTime = view.findViewById(R.id.tvDateTime);
            btnInvoice = view.findViewById(R.id.btnInvoice);
            btnShare = view.findViewById(R.id.btnShare);
        }
    }

}