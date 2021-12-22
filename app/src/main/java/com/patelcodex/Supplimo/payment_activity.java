package com.patelcodex.Supplimo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.patelcodex.Supplimo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class payment_activity extends AppCompatActivity {
     TextView total_amount;
     RadioButton payment_method;
     Button place_order;
     String total,orderid;
     FirebaseDatabase fdb;
     DatabaseReference dbrf,admin;
     ImageButton imageButton;
     FirebaseAuth mAuth;
     String uid;
     HashMap<String,Object>order=new HashMap<>();
     HashMap<String,Object>adress=new HashMap<>();
     int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        total_amount=findViewById(R.id.Total_amount);
        payment_method=findViewById(R.id.cash_option);
        place_order=findViewById(R.id.place_order);
        imageButton=findViewById(R.id.close_activity_py);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        fdb=FirebaseDatabase.getInstance();
        dbrf=fdb.getReference().child("users").child(uid);
        admin=fdb.getReference().child("Admin");

        total=getIntent().getStringExtra("total");
        orderid=getIntent().getStringExtra("orderid");
        total_amount.setText(total.replace("rs",""));
        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order=(HashMap<String,Object>)getIntent().getSerializableExtra("order");
                adress=(HashMap<String,Object>)getIntent().getSerializableExtra("address");
                dbrf.child("order").updateChildren(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
//                        Toast.makeText(payment_activity.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                        place_order.setText("Placed");
                        dbrf.child("cart").removeValue();
                        place_order.setBackgroundColor(Color.parseColor("#FF018786"));
                        admin.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()){
                                    DataSnapshot dataSnapshot=task.getResult();
                                    i=(int)dataSnapshot.getChildrenCount();
                                    admin.child(Integer.toString(i+1)).child("products").updateChildren(order);
                                    admin.child(Integer.toString(i+1)).child("address").updateChildren(adress);
                                    dbrf.updateChildren(adress);
                                }
                            }
                        });
                        dbrf.child("order").child(orderid).updateChildren(adress) ;

//                        Intent intent=new Intent(payment_activity.this,cart_activity.class);

                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                            NotificationChannel channel=new NotificationChannel("notification","notification",NotificationManager.IMPORTANCE_DEFAULT);
                            NotificationManager manager=getSystemService(NotificationManager.class);
                            manager.createNotificationChannel(channel);
                        }
                        NotificationCompat.Builder mbuilder=(NotificationCompat.Builder)
                                new NotificationCompat.Builder(payment_activity.this,"notification")
                                .setSmallIcon(R.drawable.proteinlogo)
                                .setContentTitle("order")
                                .setContentText("order placed successfully")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        NotificationManagerCompat manager=NotificationManagerCompat.from(payment_activity.this);
                        Toast.makeText(payment_activity.this,"Order Placed Succesfully",Toast.LENGTH_SHORT).show();
                        manager.notify(999,mbuilder.build());

                            try {
                                Thread.sleep(1000);
//                                startActivity(intent);
                                getSupportFragmentManager().popBackStack();
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            Toast.makeText(payment_activity.this,"failed for some reason",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}