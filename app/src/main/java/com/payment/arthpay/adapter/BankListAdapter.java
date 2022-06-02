package com.payment.arthpay.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.payment.arthpay.model.BankListItems;

import java.util.List;

public class BankListAdapter extends BaseAdapter {

    private List<BankListItems> dataList;
    private LayoutInflater layoutInflater = null;
    private Context context;

    public BankListAdapter(Context context, List<BankListItems> dataList) {
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
        ImageView icon;
        public Holder(View rowView) {
            tvMenuText = (TextView) rowView.findViewById(R.id.tvOperator);
            icon =  rowView.findViewById(R.id.bankImg);
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

        final BankListItems model = dataList.get(position);
        holder.tvMenuText.setText(model.getBank());

        Glide.with(context)
                .load(model.getBank_icon())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.icon);

        convertView.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("id", model.getId());
            returnIntent.putExtra("name", model.getBank());
            returnIntent.putExtra("bankId", model.getBank_id());
            returnIntent.putExtra("ifsc", model.getIfsc());
            ((Activity)context).setResult(100, returnIntent);
            ((Activity)context).finish();
        });
        return convertView;
    }

    public void UpdateList(List<BankListItems> item)
    {
        dataList=item;
        notifyDataSetChanged();
    }
}
