package com.patelcodex.Supplimo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.patelcodex.Supplimo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class adapter extends FirebaseRecyclerAdapter<model,adapter.viewholder> {
    public adapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewholder holder, int position, @NonNull model model) {
        Glide.with(holder.imgfire.getContext()).load(model.getUrl()).into(holder.imgfire);
        holder.name.setText(model.getName());
        holder.company.setText(model.getCompany());
        holder.price.setText("₹"+model.getP1());
        holder.category.setText(model.getCategory());
             holder.itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     AppCompatActivity activity=(AppCompatActivity)v.getContext();
                     activity.getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new descfragment(model.getId(),model.getName(),model.getCompany(),model.getP1(),model.getUrl(),model.getW1(),model.getCategory())).addToBackStack(null).commit();

                 }
             });
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products,parent,false);
        return new viewholder(view);
    }

    class viewholder extends RecyclerView.ViewHolder{
        ImageView imgfire;
        TextView url,name,price,company,category,weight;
        ImageButton wishlist_btn;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            imgfire=(ImageView)itemView.findViewById(R.id.pro_img);
            name=(TextView)itemView.findViewById(R.id.pro_name);
            company=(TextView)itemView.findViewById(R.id.company_name);
            category=(TextView)itemView.findViewById(R.id.category_name);
            price=(TextView) itemView.findViewById(R.id.pro_price);
        }
    }


}
