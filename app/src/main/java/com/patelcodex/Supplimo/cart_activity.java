package com.patelcodex.Supplimo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.patelcodex.Supplimo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class cart_activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    public TextView mrpt,gst,fair,deliverycharges;
    private DatabaseReference refd;
    private Button calcu,placeorder;
    private LinearLayout linearLayout;
    private ImageButton go_back;
    private LinearLayout ll;
    CardView crd;
    String uid,phone="",name_pro="";
    int count=0,item_c=0;
    long ct=0;
    FirebaseDatabase fdb;
    DatabaseReference dbrf;
    HashMap<String,Object> ordernode=new HashMap<>();
    HashMap<String,Object> ordernodemain=new HashMap<>();
    HashMap<String,Object> temporder=new HashMap<>();

    public int total,temptotal,ki,total_amount=0,total_gst=0,total_mrp=0,id=123;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        mrpt=findViewById(R.id.mrp);
        fair=findViewById(R.id.fair);
        calcu=findViewById(R.id.calculate_total);
        recyclerView=findViewById(R.id.cart_recyclerview);
        linearLayout=findViewById(R.id.linear);
        go_back=findViewById(R.id.go_back);
        crd=findViewById(R.id.price_dis);
        ll=findViewById(R.id.add_item_cart);
        ll.setVisibility(View.INVISIBLE);
        deliverycharges=findViewById(R.id.delivery_charges);
        deliverycharges.setText("₹0");

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getSupportFragmentManager().popBackStack();
            }
        });

        placeorder=findViewById(R.id.place_order);
        placeorder.setVisibility(View.INVISIBLE);


        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        recyclerView.setLayoutManager(new LinearLayoutManager(cart_activity.this));
        recyclerView.setHasFixedSize(false);
        fdb=FirebaseDatabase.getInstance();
        dbrf=fdb.getReference("users");

        calcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    calculate_total();
                    if(total_mrp>0) {
                        placeorder.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(cart_activity.this,"add something to cart",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(cart_activity.this,e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==1){
                    dbrf.child(uid).child("order").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful()){
                                DataSnapshot ds=task.getResult();
                                ct=ds.getChildrenCount()+1;
                                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                ordernodemain.put(Long.toString(ct),ordernode);
                                ordernode.put("items",temporder);
                                ordernode.put("date",date);
                                ordernode.put("code","0");
                                ordernode.put("status","will deliver within 2 days");
                                ordernode.put("orderid",Long.toString(ct));
                                ordernode.put("ordtotal",Integer.toString(total_amount));
                                ordernode.put("name",name_pro);
                                Intent intent=new Intent(cart_activity.this,details_activity.class);
                                intent.putExtra("totalprice",fair.getText().toString());
                                intent.putExtra("order",ordernodemain);
                                intent.putExtra("orderid",Long.toString(ct));
                                startActivity(intent);
//                                          Toast.makeText(cart_activity.this,Long.toString(ct),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onStart() {
        total_mrp=0;
        super.onStart();
        final DatabaseReference dbrf=FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("cart");
        dbrf.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot ds=task.getResult();
                    item_c= (int) ds.getChildrenCount();
                    if(item_c==0){
//                        Toast.makeText(cart_activity.this,"add item to cart",Toast.LENGTH_SHORT).show();
                        crd.setVisibility(View.INVISIBLE);
                        calcu.setVisibility(View.INVISIBLE);
                        ll.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        FirebaseRecyclerOptions<cart_model> options=
                new FirebaseRecyclerOptions.Builder<cart_model>()
                        .setQuery(dbrf,cart_model.class).build();
        FirebaseRecyclerAdapter<cart_model,card_viewholder> adapter=new FirebaseRecyclerAdapter<cart_model,card_viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull card_viewholder holder, int position, @NonNull cart_model model) {
                Glide.with(holder.imgfirebase.getContext()).load(model.getUrl()).into(holder.imgfirebase);
                holder.name.setText(model.getName());
                holder.company.setText(model.getCompany());
                holder.price.setText("₹"+model.getPrice());
                holder.quant.setText(model.getQuant());
                name_pro=name_pro+model.getCategory()+"\n";
                holder.weight.setText("("+model.getWeight()+")");
                int single_item=(Integer.valueOf(model.getPrice()))*(Integer.valueOf(model.getQuant()));
                total_mrp=total_mrp+single_item;
                HashMap<String,String> order=new HashMap<>();

                order.put("id",model.getItem());
                order.put("quant",model.getQuant());
                order.put("weight",model.getWeight());
//                order.put("status","will deliver within 2 days");
//                order.put("code","0");
                order.put("price",Integer.toString(single_item));
                temporder.put(model.getItem(),order);
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pro_id=model.getItem();
                        Bundle args = new Bundle();
                        args.putString("pro_id",pro_id);
                        cart_delete delete=new cart_delete();
                        delete.setArguments(args);
                        delete.show(getSupportFragmentManager(),"delete");
                    }
                });
                holder.quant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle=new Bundle();
                        bundle.putString("item",model.getItem());
                        cart_set_quantity quant_frag=new cart_set_quantity();
                        quant_frag.setArguments(bundle);
                        quant_frag.show(getSupportFragmentManager(),"quant_frag");

                    }
                });
            }

            @NonNull
            @Override
            public card_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_products,parent,false);
                card_viewholder holder=new card_viewholder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
    @Override
    public void onResume(){
        mrpt.setText("₹"+Integer.toString(total_mrp));
        total_amount=total_gst+total_mrp;
        fair.setText("₹"+Integer.toString(total_amount));
//        Toast.makeText(cart_activity.this,Integer.toString(item_c),Toast.LENGTH_SHORT).show();
        super.onResume();
    }

    @Override
    public  void onPause(){
        total_mrp=0;
        count=0;
        placeorder.setVisibility(View.INVISIBLE);
        super.onPause();
    }

    public void calculate_total(){
        count=1;
        mrpt.setText("₹"+Integer.toString(total_mrp));
        if(total_mrp>0 && total_mrp<500){
            total_amount=total_mrp+40;
            deliverycharges.setText("₹40");
        }
        else {
            total_amount=total_mrp;
            deliverycharges.setText("free");
            deliverycharges.setTextColor(getResources().getColor(R.color.teal_700));
        }

        fair.setText("₹"+Integer.toString(total_amount));
    }

}