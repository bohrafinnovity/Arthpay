package com.payment.arthpay.RechargePlans.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.payment.arthpay.R;
import com.payment.arthpay.app.Constents;

import java.util.List;

public class PlanTitleAdapter extends
        RecyclerView.Adapter<PlanTitleAdapter.MyViewHolder> {
    private List<String> dataList;
    TitleItemClick listener;
    Context context;

    public PlanTitleAdapter(List<String> dataList, Context context, TitleItemClick listener) {
        this.dataList = dataList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_plans_title_row, parent, false);
        MyViewHolder gvh = new MyViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.planName.setText(dataList.get(position));
        holder.planName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.ontitleItemClick(position);
                Constents.GLOBAL_POSITION = position;
                notifyDataSetChanged();
            }
        });

        if ( Constents.GLOBAL_POSITION == position) {
            holder.viewBg.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.viewBg.setBackgroundColor(context.getResources().getColor(R.color.GrayLight));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView planName;
        View viewBg;
        public MyViewHolder(View view) {
            super(view);
            planName = view.findViewById(R.id.planName);
            viewBg = view.findViewById(R.id.viewBg);

        }
    }

    public interface TitleItemClick {
        void ontitleItemClick(int position);
    }
}