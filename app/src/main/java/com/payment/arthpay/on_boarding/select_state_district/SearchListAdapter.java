package com.payment.arthpay.on_boarding.select_state_district;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.arthpay.databinding.StateListItemBinding;
import com.payment.arthpay.on_boarding.district_model.DistrictModel;
import com.payment.arthpay.on_boarding.district_model.state_model.StateModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchList_VH> {

    private Context mContext;
    private List<StateModel> dataList;
    private List<DistrictModel> districtDataList;
    OnSearchListClickListner listner;
    private String type;

    public SearchListAdapter(Context mContext, List<StateModel> dataList, OnSearchListClickListner listner, String type) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.listner = listner;
        this.type = type;
    }

    public SearchListAdapter(List<DistrictModel> districtDataList, OnSearchListClickListner listner, String type) {
        this.districtDataList = districtDataList;
        this.listner = listner;
        this.type = type;
    }

    public void UpdateList(List<StateModel> temp) {
        this.dataList = temp;
        notifyDataSetChanged();
    }

    public void UpdateDistrictList(List<DistrictModel> temp) {
        this.districtDataList = temp;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public SearchList_VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        StateListItemBinding binding = StateListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchList_VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchList_VH holder, int position) {

        if (type.equalsIgnoreCase("state")) {
            StateModel data = dataList.get(position);
            holder.name.setText(data.getStatename());
            holder.listLayout.setOnClickListener(v -> {
                listner.onSearchItemClicked(position, data.getStateid(), data.getStatename());
            });
        }

        if (type.equalsIgnoreCase("district")) {
            DistrictModel data = districtDataList.get(position);
            holder.name.setText(data.getDistrictname());
            holder.listLayout.setOnClickListener(v -> {
                listner.onDistrictItemClicked(position, data.getDistrictname(),data.getDistrictid());
            });
        }


    }

    @Override
    public int getItemCount() {
        if (type.equalsIgnoreCase("state")) {
            return dataList.size();
        }else {
            return districtDataList.size();
        }

    }

    class SearchList_VH extends RecyclerView.ViewHolder {

        TextView name;
        ConstraintLayout listLayout;

        public SearchList_VH(StateListItemBinding binding) {
            super(binding.getRoot());
            name = binding.name;
            listLayout = binding.listLayout;

        }
    }

    interface OnSearchListClickListner {
        void onSearchItemClicked(int postion, String stateId, String stateName);

        void onDistrictItemClicked(int postion, String districtname,String districtId);
    }
}
