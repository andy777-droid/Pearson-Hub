package com.dube.ashley.pearsonhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    public int[] slider_images = {R.drawable.accounting, R.drawable.arts, R.drawable.english, R.drawable.maths, R.drawable.science, R.drawable.business};
    public String[] slider_titles = {"Accounting", "Arts and Culture", "English", "Mathematics", "Science", "Business Management"};
    public String[] slider_prices = {"750", "550", "650", "450", "350", "480"};

    @Override
    public int getCount() {
        return slider_titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView sliderimage = (ImageView) view.findViewById(R.id.slider_image);
        TextView slidertitle = (TextView) view.findViewById(R.id.slider_title);
        TextView sliderprice = (TextView) view.findViewById(R.id.slider_price);

        sliderimage.setImageResource(slider_images[position]);
        slidertitle.setText(slider_titles[position]);
        sliderprice.setText("R " + slider_prices[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
