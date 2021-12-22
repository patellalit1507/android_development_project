package com.patelcodex.Supplimo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.patelcodex.Supplimo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class view_details extends AppCompatActivity {
    RecyclerView rr;
    String orderid,date,total,uid,imgurl,add1,add2,city,pincode;
    TextView total_ord,ord_add1,ord_add2,ord_city,ord_pincode;
    DatabaseReference dbr;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        rr=findViewById(R.id.view_dtl_recycler_temp);
        total_ord=findViewById(R.id.oder_detail_total);
        ord_add1=findViewById(R.id.order_add1);
        ord_add2=findViewById(R.id.order_add2);
        ord_city=findViewById(R.id.order_city);
        ord_pincode=findViewById(R.id.order_pincode);
        back=findViewById(R.id.details_back_home);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dbr=FirebaseDatabase.getInstance().getReference();
        rr.setLayoutManager(new LinearLayoutManager(view_details.this));
        orderid=getIntent().getStringExtra("orderid");
        date=getIntent().getStringExtra("date");
        total=getIntent().getStringExtra("total");
        uid=getIntent().getStringExtra("uid");
        total_ord.setText("₹"+total);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("order").child(orderid).child("items");
        FirebaseRecyclerOptions<order_detail_model> options=new FirebaseRecyclerOptions.Builder<order_detail_model>()
                .setQuery(ref,order_detail_model.class)
                .build();
        FirebaseRecyclerAdapter<order_detail_model,order_details_viewholder> adapter=new FirebaseRecyclerAdapter<order_detail_model, order_details_viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull order_details_viewholder holder, int position, @NonNull order_detail_model model) {
                holder.qty.setText("Qty: "+model.getQuant());
                holder.item_price.setText("Price:  ₹"+model.getPrice());
                holder.order_weight.setText(model.getWeight());
                String id=model.getId();
                String temp=id.substring(0,id.length()-1);
                dbr.child("products").child(temp).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            DataSnapshot dataSnapshot=task.getResult();
                            imgurl=String.valueOf(dataSnapshot.child("url").getValue());
                            Glide.with(holder.imageView.getContext()).load(imgurl).into(holder.imageView);
                            holder.company.setText(String.valueOf(dataSnapshot.child("company").getValue()));
                            holder.name.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                            holder.category.setText(String.valueOf(dataSnapshot.child("category").getValue()));
                        }
                    }
                });
            }

            @NonNull
            @Override
            public order_details_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_view,parent,false);
                order_details_viewholder holder= new order_details_viewholder(view);
                return holder;
            }
        };
        rr.setAdapter(adapter);
        adapter.startListening();
        dbr.child("users").child(uid).child("order").child(orderid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    DataSnapshot ds = task.getResult();
                    add1=String.valueOf(ds.child("address1").getValue());
                    add2=String.valueOf(ds.child("address2").getValue());
                    city=String.valueOf(ds.child("city").getValue());
                    pincode=String.valueOf(ds.child("pincode").getValue());
                    ord_add1.setText(add1);
                    ord_add2.setText(add2);
                    ord_city.setText(city);
                    ord_pincode.setText(pincode);

                }
            }
        });

    }
}