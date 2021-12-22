package com.patelcodex.Supplimo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.patelcodex.Supplimo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class orders_activity extends AppCompatActivity {
    FirebaseAuth mAuth;
    String uid,imgurl;;
    DatabaseReference dbr,nrf;
    RecyclerView recyclerView;
    ImageButton back;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        dbr=FirebaseDatabase.getInstance().getReference();

        recyclerView=findViewById(R.id.order_recycler_view);
        back=findViewById(R.id.order_back_home);
        ll=findViewById(R.id.add_item_orders);
        ll.setVisibility(View.INVISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(orders_activity.this));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        nrf=dbr.child("users").child(uid).child("order");

    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("order");
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot ds=task.getResult();
                    int ct=(int)ds.getChildrenCount();
                    if(ct==0){
                        ll.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        FirebaseRecyclerOptions<orders_model> options=new FirebaseRecyclerOptions.Builder<orders_model>()
                .setQuery(ref.orderByChild("code").startAt("0").endAt("\uf8ff"),orders_model.class)
                .build();
        FirebaseRecyclerAdapter<orders_model,order_viewholder> adapter=new FirebaseRecyclerAdapter<orders_model, order_viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull order_viewholder holder, int position, @NonNull orders_model model) {
                String temp_name=model.getName().replace("\n"," ,");
                temp_name=temp_name.substring(0,temp_name.length()-1);
                holder.items.setText(temp_name);
                holder.date.setText(model.getDate());
                holder.order_tot.setText("₹"+model.getOrdtotal());
                String code=model.getCode();
                if(code.equals("0")) {
                    holder.status.setText(model.getStatus());
                }
                else if(code.equals("1")){
                    holder.status.setText("Delivered");
                    holder.status.setTextColor(Color.parseColor("#FF018786"));
                }
                holder.ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Intent intent=new Intent(orders_activity.this,view_details.class);
                       intent.putExtra("orderid",model.getOrderid());
                       intent.putExtra("date",model.getDate());
                       intent.putExtra("total",model.getOrdtotal());
                       intent.putExtra("uid",uid);
                       startActivity(intent);
                    }
                });


           }

            @NonNull
            @Override
            public order_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_viewholder,parent,false);
                order_viewholder holder= new order_viewholder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}