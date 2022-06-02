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
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.reports.MyUtil;
import com.payment.arthpay.reports.invoice.ReportInvoice;
import com.payment.arthpay.reports.model.WalletBankModel;
import com.payment.arthpay.reports.status.CheckStatus;
import com.payment.arthpay.utill.AppHandler;

import java.util.List;

public class WalletStatementAdapter extends RecyclerView.Adapter<WalletStatementAdapter.MyViewHolder> {
    private Context mContext;
    private List<WalletBankModel> dataList;


    public WalletStatementAdapter(Context mContext, List<WalletBankModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_report_wallet_ladger_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        WalletBankModel model = dataList.get(position);
        holder.tvTxnId.setText(model.getTxnid());
        holder.tvRDetails.setText(model.getSendername().replaceAll("<br>", " "));
        holder.tvProduct.setText(model.getProduct());
        holder.tvProvider.setText(model.getProvidername());
        holder.tvNumber.setText(model.getNumber());
        holder.tvStType.setText(model.getRtype());

        try {
            double amount = Double.parseDouble(model.getAmount());
            double openingBalance = Double.parseDouble(model.getBalance());
            double charge = Double.parseDouble(model.getCharge());
            double profit = Double.parseDouble(model.getProfit());
            double gst = Double.parseDouble(model.getGst());
            double tds = Double.parseDouble(model.getTds());
            double closingBalance = 0;
            double showAmount = 0;

            if (model.getProduct().equalsIgnoreCase("dmt")){
                if (model.getTransType().equalsIgnoreCase("credit")) {
                    closingBalance = openingBalance + (amount + charge - profit);
                } else if (model.getTransType().equalsIgnoreCase("debit")) {
                    closingBalance = openingBalance - (amount + charge - profit);
                }
            }else {
                if (model.getTransType().equalsIgnoreCase("credit")) {
                    closingBalance = openingBalance + (amount - (profit - gst - tds));
                } else if (model.getTransType().equalsIgnoreCase("debit")) {
                    closingBalance = openingBalance - (amount - (profit - gst - tds));
                }
            }

            if (model.getProduct().equalsIgnoreCase("dmt")){
                if (model.getTransType().equalsIgnoreCase("credit")) {
                    showAmount = amount + charge - profit;
                } else if (model.getTransType().equalsIgnoreCase("debit")) {
                    showAmount = amount + charge - profit;
                }
            }else{
                if (model.getTransType().equalsIgnoreCase("credit")) {
                    showAmount = amount - (profit - gst - tds);
                } else if (model.getTransType().equalsIgnoreCase("debit")) {
                    showAmount = amount - (profit - gst - tds);
                }
            }

            model.setClosingAmount(closingBalance);
            model.setShowAmount(showAmount);
            holder.tvAmount.setText(MyUtil.formatWithRupee(mContext, MyUtil.round(showAmount, 2)));
            holder.tvClosingBalance.setText(MyUtil.formatWithRupee(mContext, MyUtil.round(closingBalance, 2)));
            holder.tvOpenBalance.setText(MyUtil.formatWithRupee(mContext, openingBalance));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvStatus.setText(model.getStatus());
        holder.tvDateTime.setText(model.getCreatedAt());
        holder.btnConfirm.setVisibility(View.GONE);
        holder.btnConfirm.setOnClickListener(v -> {
            Intent i = new Intent(mContext, CheckStatus.class);
            i.putExtra("typeValue", "aeps");
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
            AppHandler.initWalletStatement(model, mContext, model.getClosingAmount(), model.getShowAmount());
            Intent i = new Intent(mContext, ReportInvoice.class);
            i.putExtra("status", model.getStatus());
            i.putExtra("remark", "" + model.getSendername());
            mContext.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTxnId, tvStatus, tvDateTime, tvRDetails, tvProduct, tvProvider, tvNumber,
                tvStType, tvClosingBalance, tvOpenBalance, tvAmount;
        public ImageView btnInvoice, btnConfirm;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            tvTxnId = view.findViewById(R.id.tvTxnId);
            tvStatus = view.findViewById(R.id.tvStatus);
            cardView = view.findViewById(R.id.cardView);
            tvRDetails = view.findViewById(R.id.tvRDetails);
            tvProduct = view.findViewById(R.id.tvProduct);
            tvProvider = view.findViewById(R.id.tvProvider);
            tvNumber = view.findViewById(R.id.tvNumber);
            tvStType = view.findViewById(R.id.tvStType);
            tvClosingBalance = view.findViewById(R.id.tvClosingBalance);
            tvOpenBalance = view.findViewById(R.id.tvOpenBalance);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvDateTime = view.findViewById(R.id.tvDateTime);
            btnInvoice = view.findViewById(R.id.btnInvoice);
            btnConfirm = view.findViewById(R.id.btnConfirm);
        }
    }

}