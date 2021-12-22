package com.patelcodex.Supplimo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.patelcodex.Supplimo.R;

public class order_details_viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView imageView;
    public TextView name,company,category,qty,item_price,order_weight;

    public order_details_viewholder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.order_img);
        name=itemView.findViewById(R.id.order_name);
        company=itemView.findViewById(R.id.order_company);
        category=itemView.findViewById(R.id.order_category);
        qty=itemView.findViewById(R.id.order_qty);
        item_price=itemView.findViewById(R.id.order_price);
        order_weight=itemView.findViewById(R.id.order_weight);
    }

    @Override
    public void onClick(View v) {

    }
}
