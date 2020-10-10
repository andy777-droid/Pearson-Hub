package com.dube.ashley.pearsonhub;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class MyViewHolder extends RecyclerView.ViewHolder
{
    ImageView thumbnail,listingsImageView, WishlistImageView;
    TextView title,price,listingsTitle, wishlistTitle, listingsPrice, wishlistPrice;
    CardView cardView,cardView2, cardView3;
    Button editButton, removeButton, removeButton2;
    public MyViewHolder(@NonNull View itemView)
    {
        //Search
        super(itemView);
        thumbnail=itemView.findViewById(R.id.book_img_id);
        title=itemView.findViewById(R.id.book_title_id);
        price=itemView.findViewById(R.id.book_Price_id);
        cardView=(CardView) itemView.findViewById(R.id.cardViewBook_id);

        //BookListings
        listingsImageView=itemView.findViewById(R.id.listingsImageView);
        listingsTitle=itemView.findViewById(R.id.listingsTitle);
        listingsPrice=itemView.findViewById(R.id.listingsPrice);
        cardView2=(CardView) itemView.findViewById(R.id.cardViewListings_id);
        editButton = (Button) itemView.findViewById(R.id.editButton);
        removeButton = (Button) itemView.findViewById(R.id.removeButton);

        //Wishlist
        WishlistImageView=itemView.findViewById(R.id.wishlistImageView);
        wishlistTitle=itemView.findViewById(R.id.wishlistTitle);
        wishlistPrice=itemView.findViewById(R.id.wishlistPrice);
        cardView3=(CardView) itemView.findViewById(R.id.cardViewWishlist_id);
        removeButton2 = (Button) itemView.findViewById(R.id.removeButton2);
    }
}
