package com.payment.arthpay.reports.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.reports.MyUtil;
import com.payment.arthpay.reports.invoice.ReportInvoice;
import com.payment.arthpay.reports.model.AEPSReportModel;
import com.payment.arthpay.reports.status.CheckStatus;
import com.payment.arthpay.utill.AppHandler;

import java.util.List;

public class AEPSTranAdapter extends RecyclerView.Adapter<AEPSTranAdapter.MyViewHolder> {
    private Context mContext;
    private List<AEPSReportModel> dataList;
    private String type;

    public AEPSTranAdapter(Context mContext, List<AEPSReportModel> dataList, String type) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.type = type;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_report_aeps_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        AEPSReportModel model = dataList.get(position);
        holder.tvTxnId.setText(model.getTxnid());
        holder.tvMobile.setText(model.getMobile());
        holder.tvBCID.setText(model.getAadhar());
        holder.tvCharge.setText(MyUtil.formatWithRupee(mContext, model.getCharge()));
        holder.tvBalance.setText(MyUtil.formatWithRupee(mContext, model.getBalance()));
        holder.tvGST.setText(MyUtil.formatWithRupee(mContext, model.getGst()));
        holder.tvTDS.setText(MyUtil.formatWithRupee(mContext, model.getTds()));
        String b = MyUtil.formatWithRupee(mContext, model.getAmount());
        if (MyUtil.isNN(model.getAepstype())){
            holder.secAeps.setVisibility(View.VISIBLE);
            holder.tvAepsType.setText(model.getAepstype());
        }else{
            holder.secAeps.setVisibility(View.GONE);
        }
        holder.TVAmount.setText(b);
        holder.tvType.setText(model.getType());
        holder.tvStatus.setText(model.getStatus());
        holder.tvDateTime.setText(model.getCreatedAt());

        if (model.getStatus().contains("pending")) {
            holder.btnConfirm.setVisibility(View.VISIBLE);
        }else {
            holder.btnConfirm.setVisibility(View.GONE);
        }
        holder.btnConfirm.setOnClickListener(v -> {
            Intent i = new Intent(mContext, CheckStatus.class);
            if (type.equalsIgnoreCase("aepsstatement")) {
                i.putExtra("typeValue", "aeps");
            } else {
                i.putExtra("typeValue", "matm");
            }
            i.putExtra("id", model.getId());
            i.putExtra("txnId", model.getTxnid());
            i.putExtra("url", Constents.URL.REPORT_CHECK_STATUS);
            mContext.startActivity(i);
        });

        switch (model.getStatus()) {
            case "success":
            case "Success":
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
                //holder.btnConfirm.setVisibility(View.VISIBLE);
        }

        holder.btnInvoice.setOnClickListener(v -> {
            AppHandler.initAepsTransactionReportData(model, mContext);
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
        public TextView tvTxnId, tvMobile, tvBCID, TVAmount, tvCharge, tvBalance, tvType, tvStatus,
                tvDateTime, tvAepsType, tvGST, tvTDS;
        public LinearLayout secAeps ;
        public ImageView btnInvoice, btnShare, btnConfirm;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            secAeps = view.findViewById(R.id.secAeps);
            tvGST = view.findViewById(R.id.tvGST);
            tvTDS = view.findViewById(R.id.tvTDS);
            tvAepsType = view.findViewById(R.id.tvAepsType);
            tvTxnId = view.findViewById(R.id.tvTxnId);
            cardView = view.findViewById(R.id.cardView);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvBCID = view.findViewById(R.id.tvBCID);
            TVAmount = view.findViewById(R.id.TVAmount);
            tvCharge = view.findViewById(R.id.tvCharge);
            tvBalance = view.findViewById(R.id.tvBalance);
            tvType = view.findViewById(R.id.tvType);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvDateTime = view.findViewById(R.id.tvDateTime);
            btnInvoice = view.findViewById(R.id.btnInvoice);
            btnShare = view.findViewById(R.id.btnShare);
            btnConfirm = view.findViewById(R.id.btnConfirm);
            //btnConfirm.setVisibility(View.GONE);
        }
    }

}