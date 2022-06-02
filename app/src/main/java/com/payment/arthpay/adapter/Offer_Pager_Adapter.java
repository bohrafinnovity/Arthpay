package com.payment.arthpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.payment.arthpay.R;

import java.util.List;


public class Offer_Pager_Adapter extends PagerAdapter {
    Context context;
    private List<Integer> bannerDrawables;
    List<String> bannerUrl;
    int bannerAvail;
    public static int LOOPS_COUNT = 1000;

    LayoutInflater mLayoutInflater;

    public Offer_Pager_Adapter(Context context, List<Integer> bannerDrawables, List<String>bannerUrl, int bannerAvail) {
        this.context = context;
        this.bannerDrawables = bannerDrawables;
        this.bannerUrl = bannerUrl;
        this.bannerAvail = bannerAvail;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if(bannerAvail==1)
            return bannerUrl.size()*LOOPS_COUNT;;
        return bannerDrawables.size()*LOOPS_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_element, container, false);

        if(bannerAvail==1)
            position = position % bannerUrl.size();
        else
            position = position % bannerDrawables.size();

        ImageView imageView = itemView.findViewById(R.id.image);


        if(bannerAvail==0)
            imageView.setImageDrawable(context.getResources().getDrawable(bannerDrawables.get(position)));
        else
            Glide.with(context)
                    .load(bannerUrl.get(position))
                    .into(imageView);


        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}