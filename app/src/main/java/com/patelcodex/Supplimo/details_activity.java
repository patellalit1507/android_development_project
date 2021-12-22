package com.patelcodex.Supplimo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.patelcodex.Supplimo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class details_activity extends AppCompatActivity {
    TextView fullname,phone;
    EditText address1,address2,city,pincode,emailaddress;
    Button submit;
    FirebaseAuth mAuth;
    FirebaseDatabase fdb;
    DatabaseReference dbrf;
    ImageButton go_back;
    String user_uid,user_name,full_name,emial_address,pho_ne,chan_geno,addre_1,addre_2,pin_code,cit_y,total,orderid;
    HashMap<String,String> order=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        fullname=findViewById(R.id.full_name);
        emailaddress=findViewById(R.id.email_address);
        phone=findViewById(R.id.phone_number);
        address1=findViewById(R.id.address_line_1);
        address2=findViewById(R.id.address_line_2);
        city=findViewById(R.id.city);
        pincode=findViewById(R.id.pincode);
        submit=findViewById(R.id.submit_details);
        go_back=findViewById(R.id.close_activity);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        total=getIntent().getStringExtra("totalprice");
        orderid=getIntent().getStringExtra("orderid");
        order=(HashMap<String,String>)getIntent().getSerializableExtra("order");

        mAuth=FirebaseAuth.getInstance();
        fdb=FirebaseDatabase.getInstance();
        dbrf=fdb.getReference("users");


        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null) {
            user_uid = mAuth.getCurrentUser().getUid().toString();
            dbrf.child(user_uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        DataSnapshot dataSnapshot=task.getResult();
                        full_name=String.valueOf(dataSnapshot.child("fname").getValue())+" "+String.valueOf(dataSnapshot.child("lname").getValue());
                        emial_address=String.valueOf(dataSnapshot.child("email").getValue());
                        pho_ne=String.valueOf(dataSnapshot.child("phone").getValue());
                        fullname.setText(full_name);
                        if(!emial_address.equals("null")){
                            emailaddress.setText(emial_address);
                        }

                        phone.setText(pho_ne);
                    }
                }
            });
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 addre_1=address1.getText().toString();
                 addre_2=address2.getText().toString();
                 pin_code=pincode.getText().toString();
                 cit_y=city.getText().toString();
                 if(addre_1.isEmpty() && addre_2.isEmpty() && pin_code.isEmpty() && cit_y.isEmpty()){
                     Toast.makeText(details_activity.this,"Something is missing in address",Toast.LENGTH_SHORT).show();
                 }
                 else if(pin_code.length()!=6){
                     Toast.makeText(details_activity.this,"Check Pincode",Toast.LENGTH_SHORT).show();
                 }
                 else{
                     user_uid=mAuth.getCurrentUser().getUid();
                     HashMap<String,String> address=new HashMap<>();
                     address.put("address1",addre_1);
                     address.put("address2",addre_2);
                     address.put("city",cit_y);
                     address.put("pincode",pin_code);
                     address.put("uid",user_uid);
                     Intent intent=new Intent(details_activity.this,payment_activity.class);
                     intent.putExtra("address",address);
                     intent.putExtra("total",total);
                     intent.putExtra("order",order);
                     intent.putExtra("orderid",orderid);
                     startActivity(intent);
                     finish();

                 }
            }
        });
    }
}