package com.payment.arthpay.bppsServices.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;


import com.payment.arthpay.R;
import com.payment.arthpay.bppsServices.model.ParameItem;

import java.util.List;

public class BillFetchParamListAdapter extends BaseAdapter {

    private List<ParameItem> dataList;
    private LayoutInflater layoutInflater = null;
    private Context context;
    private ParameItem model;
    private GetListDataLis listener;

    public BillFetchParamListAdapter(Context context, List<ParameItem> dataList, GetListDataLis listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
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
        TextView tvLabel;
        TextView tvValidation;
        EditText etValue;

        public Holder(View rowView) {
            etValue = rowView.findViewById(R.id.etValue);
            tvLabel = rowView.findViewById(R.id.tvLabel);
            tvValidation = rowView.findViewById(R.id.tvValidation);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SpannableStringBuilder builder;
        final Holder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fetch_bill_list_row, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final ParameItem model = dataList.get(position);
        holder.tvLabel.setText(model.getParamname());
        //String msg = "Maximum length for " + model.getParamname().toLowerCase() + " is " + model.getMaxlength();
        //holder.tvValidation.setText(msg);

       //InputFilter[] fArray = new InputFilter[1];
       // fArray[0] = new InputFilter.LengthFilter(Integer.parseInt(model.getMaxlength()));
       // holder.etValue.setFilters(fArray);

        holder.etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                model.setFieldInputValue(holder.etValue.getText().toString());
                listener.getRecords(position, model);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return convertView;
    }

    public void UpdateList(List<ParameItem> item) {
        dataList = item;
        notifyDataSetChanged();
    }

    public interface GetListDataLis {
        void getRecords(int position, ParameItem item);
    }


}
