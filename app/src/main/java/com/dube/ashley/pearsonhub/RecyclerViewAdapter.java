package com.dube.ashley.pearsonhub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.MyViewHolder>
{
    private Context mContext;
    private List<CategoryHandler> mData;

    public RecyclerViewAdapter(Context mContext, List<CategoryHandler> mData)
    {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view;
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.cardview_item_book,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
    {
        holder.tv_book_title.setText(mData.get(position).getTitle());
        holder.tv_book_price.setText("R "+mData.get(position).getPrice());
        holder.img_book_thumbnail.setImageResource(mData.get(position).getThumbnail());

        //click listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(mContext,Book.class);
                intent.putExtra("Thumbnail",mData.get(position).getThumbnail());
                intent.putExtra("Title",mData.get(position).getTitle());
                intent.putExtra("Price",mData.get(position).getPrice());
                intent.putExtra("SellerNumber",mData.get(position).getSellerNumber());
                intent.putExtra("SellerName",mData.get(position).getSellerName());
                intent.putExtra("Author",mData.get(position).getAuthor());
                intent.putExtra("Condition",mData.get(position).getCondition());
                intent.putExtra("ISBN",mData.get(position).getISBN());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_book_title;
        TextView tv_book_price;
        ImageView img_book_thumbnail;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_book_title=(TextView) itemView.findViewById(R.id.book_title_id);
            tv_book_price=(TextView) itemView.findViewById(R.id.book_Price_id);
            img_book_thumbnail=(ImageView) itemView.findViewById(R.id.book_img_id);
            cardView=(CardView) itemView.findViewById(R.id.cardViewBook_id);
        }
    }
}
