package com.dube.ashley.pearsonhub;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ImageAdapter extends PagerAdapter {

    private Context mContext;
    private int[] images = new int[] {R.drawable.business, R.drawable.science, R.drawable.maths, R.drawable.english};
    private String[] book_names = new String[] {"Business Management", "Science", "Mathematics", "English Language"};

    ImageAdapter(Context context){
      mContext = context;
    }
    @Override
    public int getCount() {
        return images.length;
    }
    //h

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(images[position]);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
          container.removeView((ImageView) object);
    }
}
