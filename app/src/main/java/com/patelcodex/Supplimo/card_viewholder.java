package com.patelcodex.Supplimo;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.patelcodex.Supplimo.R;

public class card_viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView imgfirebase;
    public TextView name,price,company,weight;
    public Button quant;
    public ImageButton delete;

    public card_viewholder(@NonNull View itemView) {
        super(itemView);
        imgfirebase=(ImageView)itemView.findViewById(R.id.cart_img);
        name=(TextView)itemView.findViewById(R.id.cart_name);
        price=(TextView)itemView.findViewById(R.id.cart_price);
        company=(TextView)itemView.findViewById(R.id.cart_company_name);
        quant=(Button) itemView.findViewById(R.id.cart_quantity);
        delete=(ImageButton)itemView.findViewById(R.id.delete_btn);
        weight=(TextView) itemView.findViewById(R.id.item_w);
    }

    @Override
    public void onClick(View v) {

    }
}
