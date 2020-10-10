package com.dube.ashley.pearsonhub;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.squareup.picasso.Picasso;
import java.util.List;

public class SliderAdapter extends PagerAdapter
{

    Context context;
    LayoutInflater layoutInflater;
    private List<CategoryHandler> catBooks;
    String theEmail;

    public SliderAdapter(Context context, List<CategoryHandler> catBooks, final String theEmail){
        this.theEmail = theEmail;
        this.context = context;
        this.catBooks=catBooks;
    }


    @Override
    public int getCount() {
        return catBooks.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position)
    {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView sliderimage = (ImageView) view.findViewById(R.id.slider_image);
        TextView slidertitle = (TextView) view.findViewById(R.id.slider_title);
        TextView sliderprice = (TextView) view.findViewById(R.id.slider_price);
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

        sliderimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(context,Book.class);
                intent.putExtra("Thumbnail",catBooks.get(position).getThumbnail());
                intent.putExtra("Title",catBooks.get(position).getTitle());
                intent.putExtra("Price",catBooks.get(position).getPrice());
                intent.putExtra("SellerNumber",catBooks.get(position).getSellerNumber());
                intent.putExtra("SellerName",catBooks.get(position).getSellerName());
                intent.putExtra("Author",catBooks.get(position).getAuthor());
                intent.putExtra("Condition",catBooks.get(position).getCondition());
                intent.putExtra("ISBN",catBooks.get(position).getISBN());
                intent.putExtra("email",theEmail);
                intent.putExtra("category",catBooks.get(position).getCategory());
                intent.putExtra("bookID",catBooks.get(position).getBookID());

                context.startActivity(intent);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }


}
