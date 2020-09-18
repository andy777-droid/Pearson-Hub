package com.dube.ashley.pearsonhub;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class MyViewHolder extends RecyclerView.ViewHolder
{
    ImageView thumbnail;
    TextView title,price;
    CardView cardView;

    public MyViewHolder(@NonNull View itemView)
    {
        super(itemView);
        thumbnail=itemView.findViewById(R.id.book_img_id);
        title=itemView.findViewById(R.id.book_title_id);
        price=itemView.findViewById(R.id.book_Price_id);
        cardView=(CardView) itemView.findViewById(R.id.cardViewBook_id);
    }
}
