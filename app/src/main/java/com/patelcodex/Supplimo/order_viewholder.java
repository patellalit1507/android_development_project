package com.patelcodex.Supplimo;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.patelcodex.Supplimo.R;

public class order_viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView status,items,order_tot,date;
    public LinearLayout ll;

    public order_viewholder(@NonNull View itemView) {
        super(itemView);
        status=itemView.findViewById(R.id.status_text);
        items=itemView.findViewById(R.id.items_name);
        order_tot=itemView.findViewById(R.id.order_total);
        date=itemView.findViewById(R.id.order_date);
        ll=itemView.findViewById(R.id.linaer_details);
    }

    @Override
    public void onClick(View v) {

    }
}
