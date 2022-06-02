package com.payment.arthpay.reports.invoice;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.payment.arthpay.R;
import com.payment.arthpay.reports.invoice.model.InvoiceModel;

import java.util.List;

public class InvoiceListAdapter extends BaseAdapter {

    private List<InvoiceModel> dataMap;
    private LayoutInflater layoutInflater = null;
    private Context context;

    public InvoiceListAdapter(Context context, List<InvoiceModel> dataMap) {
        this.context = context;
        this.dataMap = dataMap;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataMap.size();
    }

    @Override
    public Object getItem(int i) {
        return dataMap.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SpannableStringBuilder builder;
        final Holder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.m_invoice_row_layout, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        InvoiceModel model = dataMap.get(position);
        holder.title.setText(model.getKey());
        holder.value.setText(model.getValue());

        return convertView;
    }

    public static class Holder {
        TextView value, title;
        ImageView icon;

        public Holder(View rowView) {
            value = (TextView) rowView.findViewById(R.id.tvValue);
            title = (TextView) rowView.findViewById(R.id.title);
        }
    }

}
