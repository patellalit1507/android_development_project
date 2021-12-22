package com.patelcodex.Supplimo;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.patelcodex.Supplimo.R;

public class company_viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView imageView;
    public company_viewholder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.company_photo);
    }

    @Override
    public void onClick(View v) {

    }
}
