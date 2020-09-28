package com.dube.ashley.pearsonhub;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SliderAdapter extends PagerAdapter
{

    Context context;
    LayoutInflater layoutInflater;
    private List<CategoryHandler> catBooks;

    public SliderAdapter(Context context, List<CategoryHandler> catBooks){
        this.context = context;
        this.catBooks=catBooks;
    }


    //public int[] slider_images = {R.drawable.accounting, R.drawable.arts, R.drawable.english, R.drawable.maths, R.drawable.science, R.drawable.business};
   // public String[] slider_titles = {"Accounting", "Arts and Culture", "English", "Mathematics", "Science", "Business Management"};
    //public String[] slider_prices = {"750", "550", "650", "450", "350", "480"};

    @Override
    public int getCount() {
        System.out.println("The size of the List: "+catBooks.size());
        return catBooks.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {
        System.out.println("The first link: "+catBooks.get(0).getThumbnail());
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView sliderimage = (ImageView) view.findViewById(R.id.slider_image);
        TextView slidertitle = (TextView) view.findViewById(R.id.slider_title);
        TextView sliderprice = (TextView) view.findViewById(R.id.slider_price);

        //sliderimage.setImageResource(slider_images[position]);
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
            }
        });

        builder.build().load(catBooks.get(position).getThumbnail()).into(sliderimage);
        slidertitle.setText(catBooks.get(position).getTitle());
        sliderprice.setText("R " + catBooks.get(position).getPrice());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }


}
