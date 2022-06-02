package com.payment.arthpay.adapter;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.payment.arthpay.R;
import com.payment.arthpay.model.BeneficiaryItems;

import java.util.List;

public class BenCardAdapter extends RecyclerView.Adapter<BenCardAdapter.ViewHolder> {
    List<BeneficiaryItems> beneficiary_items;
    private Context context;
    private BenCardClickEvents listener;

    AlertDialog dialog;


    public BenCardAdapter(Context context, List<BeneficiaryItems> beneficiary_items,
                          BenCardClickEvents listener) {
        super();
        this.context = context;
        this.beneficiary_items = beneficiary_items;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return beneficiary_items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_beneficiary_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        BeneficiaryItems items = beneficiary_items.get(position);
        holder.textView_beneficiary_name.setText(items.getBenename());
        holder.textView_acountno.setText(items.getBeneaccount());
        holder.textView_bankname.setText(items.getBenebank());
        holder.textView_ifsc.setText(items.getBeneifsc());
        holder.textView_recepient_id.setText(items.getBeneid());

        if (items.getBenestatus().equalsIgnoreCase("NV")) {
            holder.button_verify.setVisibility(View.VISIBLE);
            holder.button_transfer.setVisibility(View.GONE);
        } else {
            holder.button_verify.setVisibility(View.GONE);
            holder.button_transfer.setVisibility(View.VISIBLE);
        }

        holder.button_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.transferListener(items);
            }
        });

        holder.button_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.verifyListener(items);
            }
        });

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_beneficiary_name;
        public TextView textView_acountno;
        public TextView textView_bankname;
        public TextView textView_ifsc;
        public TextView textView_recepient_id;
        Button button_transfer, button_verify;

        public ViewHolder(View view) {
            super(view);
            textView_beneficiary_name = view.findViewById(R.id.textview_beneficiary_name);
            textView_acountno = view.findViewById(R.id.textview_acountno);
            textView_bankname = view.findViewById(R.id.textview_bankname);
            textView_ifsc = view.findViewById(R.id.textView_ifsc);
            textView_recepient_id = view.findViewById(R.id.textView_recepient_id);
            button_transfer = view.findViewById(R.id.button_transfer);
            button_verify = view.findViewById(R.id.button_verify);
        }
    }

    public interface BenCardClickEvents {
        public void verifyListener(BeneficiaryItems item);

        public void transferListener(BeneficiaryItems item);
    }
}
