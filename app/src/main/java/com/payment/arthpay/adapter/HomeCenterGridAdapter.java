package com.payment.arthpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.arthpay.R;
import com.payment.arthpay.model.CenterGridModel;

import java.util.List;

public class HomeCenterGridAdapter extends RecyclerView.Adapter<HomeCenterGridAdapter.MyViewHolder> {

    private List<CenterGridModel> moviesList;
    private Context mContext;
    public OnItemClick listener;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;
        public CardView cardHolder;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            //count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.imgSrc);
            cardHolder =  view.findViewById(R.id.cardHolder);
            // overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public HomeCenterGridAdapter(Context mContext, List<CenterGridModel> moviesList, OnItemClick listener) {
        this.moviesList = moviesList;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.center_grid_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        CenterGridModel movie = moviesList.get(position);
        holder.title.setText(movie.getBottomLbl());
        holder.thumbnail.setImageDrawable(mContext.getResources().getDrawable(movie.getImgUrl()));
        holder.cardHolder.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public interface OnItemClick{
        void onItemClick(int position);
    }
}