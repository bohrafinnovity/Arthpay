package com.payment.arthpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.payment.arthpay.R;

import java.util.List;


public class HomeFragOfferSliderAdapter extends RecyclerView.Adapter<HomeFragOfferSliderAdapter.MyViewHolder> {

    // Class context
    private Context mContext;
    private List<Integer> dataList;

    public HomeFragOfferSliderAdapter(Context mContext, List<Integer> videoDataItems) {
        this.mContext = mContext;
        this.dataList = videoDataItems;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView pic;
        TextView name, about;

        private MyViewHolder(View view) {
            super(view);
            pic = view.findViewById(R.id.pic);
        }
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_slider_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.pic.setImageDrawable(mContext.getResources().getDrawable(dataList.get(position)));

        // Loading profile image
//        Glide.with(mContext).load(model.getPic())
//                .crossFade()
//                .thumbnail(0.5f)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
