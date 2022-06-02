package com.payment.arthpay.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.payment.arthpay.R;
import com.payment.arthpay.member.model.AppMember;

import java.util.List;

public class AllMemberAdapter extends RecyclerView.Adapter<AllMemberAdapter.ViewHolder> {

    private Context context;
    private List<AppMember> activityListModels;
    private ItemClick clickLis;

    public AllMemberAdapter(Context context, List<AppMember> activityListModels, ItemClick clickLis) {
        this.context = context;
        this.activityListModels = activityListModels;
        this.clickLis = clickLis;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_all_member_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        AppMember model = activityListModels.get(position);
        holder.tvCity.setText(model.getCity());
        holder.tvEmail.setText(model.getEmail());
        holder.tvMobile.setText(model.getMobile());
        holder.tvName.setText(model.getName());
        holder.tvParent.setText(model.getParents().replaceAll("<br>", "  "));

        holder.imgRec.setOnClickListener(v -> clickLis.clickRec(model));
        holder.imgSend.setOnClickListener(v -> clickLis.clickSend(model));
    }

    @Override
    public int getItemCount() {
        return activityListModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvMobile, tvCity, tvParent;
        ImageView imgRec, imgSend;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvParent = itemView.findViewById(R.id.tvParent);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            tvCity = itemView.findViewById(R.id.tvCity);
            imgRec = itemView.findViewById(R.id.btnRec);
            imgSend = itemView.findViewById(R.id.imgSend);

        }
    }

    public interface ItemClick {
        void clickRec(AppMember model);

        void clickSend(AppMember model);
    }
}
