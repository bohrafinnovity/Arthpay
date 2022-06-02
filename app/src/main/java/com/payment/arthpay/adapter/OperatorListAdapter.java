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
import com.payment.arthpay.model.Operators_Items;

import java.util.List;

public class OperatorListAdapter extends BaseAdapter {

    private List<Operators_Items> dataList;
    private LayoutInflater layoutInflater = null;
    private Context context;
    private String type,title,lable,desc;
    private Operators_Items model;

    public OperatorListAdapter(Context context, List<Operators_Items> dataList,String type,String title,String lable,String desc) {
        this.context = context;
        this.dataList = dataList;
        this.type = type;
        this.title = title;
        this.lable = lable;
        this.desc = desc;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public OperatorListAdapter(Context context, List<Operators_Items> dataList) {
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

        final Operators_Items model = dataList.get(position);
        holder.tvMenuText.setText(model.getOperator_name());

        if (model.getOperator_image() == null || model.getOperator_image().isEmpty() ||
                model.getOperator_image().equals("null")) {
            holder.icon.setVisibility(View.GONE);
        } else {
            Glide.with(context)
                    .load(model.getOperator_image())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.icon);
        }
        convertView.setOnClickListener(v -> {
            if(type.equalsIgnoreCase("mobile") || type.equalsIgnoreCase("dth")){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("id", model.getOperator_id());
                returnIntent.putExtra("name", model.getOperator_name());
                ((Activity) context).setResult(100, returnIntent);
                ((Activity) context).finish();
            }
            else if(type!=null && type.length()>0){
                Intent returnIntent = new Intent(context, com.payment.arthpay.newServices.BillActivity.class);
                returnIntent.putExtra("id", model.getOperator_id());
                returnIntent.putExtra("name", model.getOperator_name());
                returnIntent.putExtra("title", title);
                returnIntent.putExtra("type", type);
                returnIntent.putExtra("hint", lable);
                returnIntent.putExtra("descInput", desc);
                context.startActivity(returnIntent);
            }
            else{
                Intent returnIntent = new Intent();
                returnIntent.putExtra("id", model.getOperator_id());
                returnIntent.putExtra("name", model.getOperator_name());
                ((Activity) context).setResult(100, returnIntent);
                ((Activity) context).finish();
            }
        });

        return convertView;
    }

    public void UpdateList(List<Operators_Items> item) {
        dataList = item;
        notifyDataSetChanged();
    }
}
