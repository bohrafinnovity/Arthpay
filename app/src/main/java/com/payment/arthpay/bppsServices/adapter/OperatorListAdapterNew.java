package com.payment.arthpay.bppsServices.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.payment.arthpay.R;
import com.payment.arthpay.bppsServices.model.ProviderModel;
import com.payment.arthpay.model.Operators_Items;

import java.util.List;

public class OperatorListAdapterNew extends BaseAdapter {

    private List<ProviderModel> dataList;
    private LayoutInflater layoutInflater = null;
    private Context context;
    private Operators_Items model;

    public OperatorListAdapterNew(Context context, List<ProviderModel> dataList) {
        this.context = context;
        this.dataList = dataList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 1;
    }

    public class Holder {
        TextView tvMenuText;
        ImageView icon, goButton;

        public Holder(View rowView) {
            tvMenuText = (TextView) rowView.findViewById(R.id.tvOperator);
            icon = rowView.findViewById(R.id.bankImg);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SpannableStringBuilder builder;
        final Holder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.bank_list_row, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final ProviderModel model = dataList.get(position);
        holder.tvMenuText.setText(model.getName());

        if (model.getLogo() == null || model.getLogo().isEmpty() ||
                model.getLogo().equals("null")) {
            holder.icon.setVisibility(View.GONE);
        } else {
            Glide.with(context)
                    .load(model.getLogo())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.icon);
        }

        return convertView;
    }

    public void UpdateList(List<ProviderModel> item) {
        dataList = item;
        notifyDataSetChanged();
    }
}
